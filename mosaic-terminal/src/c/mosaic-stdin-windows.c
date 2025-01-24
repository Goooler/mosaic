#include "mosaic.h"

#if defined(_WIN32)

#include "cutils.h"
#include <windows.h>

typedef struct stdinReaderImpl {
	HANDLE waitHandles[2];
	HANDLE readHandle;
	platformEventHandler *handler;
} stdinReaderImpl;

typedef struct stdinWriterImpl {
	HANDLE readHandle;
	HANDLE writeHandle;
	HANDLE eventHandle;
	stdinReader *reader;
} stdinWriterImpl;

stdinReaderResult platformInput_initWithHandle(
	HANDLE stdinRead,
	HANDLE stdinWait,
	platformEventHandler *handler
) {
	stdinReaderResult result = {};

	stdinReaderImpl *reader = calloc(1, sizeof(stdinReaderImpl));
	if (unlikely(reader == NULL)) {
		// result.reader is set to 0 which will trigger OOM.
		goto ret;
	}

	if (unlikely(stdinRead == INVALID_HANDLE_VALUE)) {
		result.error = GetLastError();
		goto err;
	}

	HANDLE interruptEvent = CreateEvent(NULL, FALSE, FALSE, NULL);
	if (unlikely(interruptEvent == NULL)) {
		result.error = GetLastError();
		goto err;
	}

	reader->waitHandles[0] = stdinWait;
	reader->waitHandles[1] = interruptEvent;
	reader->readHandle = stdinRead;
	reader->handler = handler;

	result.reader = reader;

	ret:
	return result;

	err:
	free(reader);
	goto ret;
}

stdinReaderResult platformInput_init(platformEventHandler *handler) {
	HANDLE h = GetStdHandle(STD_INPUT_HANDLE);
	return platformInput_initWithHandle(h, h, handler);
}

stdinRead platformInput_read(
	stdinReader *reader,
	void *buffer,
	int count
) {
	return platformInput_readWithTimeout(reader, buffer, count, INFINITE);
}

stdinRead platformInput_readWithTimeout(
	stdinReader *reader,
	void *buffer,
	int count,
	int timeoutMillis
) {
	stdinRead result = {};
	DWORD waitResult = WaitForMultipleObjects(2, reader->waitHandles, FALSE, timeoutMillis);
	if (likely(waitResult == WAIT_OBJECT_0)) {
		DWORD read = 0;
		if (likely(ReadFile(reader->readHandle, buffer, count, &read, NULL) != 0)) {
			// TODO EOF?
			result.count = read;
		} else {
			goto err;
		}
	} else if (unlikely(waitResult == WAIT_FAILED)) {
		goto err;
	}
	// Else if the interrupt event was selected or we timed out, return a count of 0.

	ret:
	return result;

	err:
	result.error = GetLastError();
	goto ret;
}

platformError platformInput_interrupt(stdinReader *reader) {
	return likely(SetEvent(reader->waitHandles[1]) != 0)
		? 0
		: GetLastError();
}

platformError platformInput_free(stdinReader *reader) {
	DWORD result = 0;
	if (unlikely(CloseHandle(reader->waitHandles[1]) == 0)) {
		result = GetLastError();
	}
	free(reader);
	return result;
}

stdinWriterResult platformInputWriter_init(platformEventHandler *handler) {
	stdinWriterResult result = {};

	stdinWriterImpl *writer = calloc(1, sizeof(stdinWriterImpl));
	if (unlikely(writer == NULL)) {
		// result.writer is set to 0 which will trigger OOM.
		goto ret;
	}

	if (unlikely(CreatePipe(&writer->readHandle, &writer->writeHandle, NULL, 0) == 0)) {
		result.error = GetLastError();
		goto err;
	}

	HANDLE writeEvent = CreateEvent(NULL, FALSE, FALSE, NULL);
	if (unlikely(writeEvent == NULL)) {
		result.error = GetLastError();
		goto err;
	}
	writer->eventHandle = writeEvent;

	stdinReaderResult readerResult = platformInput_initWithHandle(writer->readHandle, writer->eventHandle, handler);
	if (unlikely(readerResult.error)) {
		result.error = readerResult.error;
		goto err;
	}
	writer->reader = readerResult.reader;

	result.writer = writer;

	ret:
	return result;

	err:
	free(writer);
	goto ret;
}

stdinReader *platformInputWriter_getReader(stdinWriter *writer) {
	return writer->reader;
}

platformError platformInputWriter_write(stdinWriter *writer, void *buffer, int count) {
	// Per https://learn.microsoft.com/en-us/windows/win32/api/namedpipeapi/nf-namedpipeapi-createpipe#remarks
	// "When a process uses WriteFile to write to an anonymous pipe,
	//  the write operation is not completed until all bytes are written."
	if (likely(WriteFile(writer->writeHandle, buffer, count, NULL, NULL)
			&& SetEvent(writer->eventHandle))) {
		return 0;
	}
	return GetLastError();
}

void platformInputWriter_focusEvent(stdinWriter *writer, bool focused) {
 	platformEventHandler *handler = writer->reader->handler;
 	handler->onFocus(handler->opaque, focused);
 }

 void platformInputWriter_keyEvent(stdinWriter *writer) {
 	platformEventHandler *handler = writer->reader->handler;
 	handler->onKey(handler->opaque);
 }

 void platformInputWriter_mouseEvent(stdinWriter *writer) {
 	platformEventHandler *handler = writer->reader->handler;
 	handler->onMouse(handler->opaque);
 }

 void platformInputWriter_resizeEvent(stdinWriter *writer, int columns, int rows, int width, int height) {
 	platformEventHandler *handler = writer->reader->handler;
 	handler->onResize(handler->opaque, columns, rows, width, height);
 }

platformError platformInputWriter_free(stdinWriter *writer) {
	DWORD result = 0;
	if (unlikely(CloseHandle(writer->eventHandle) == 0)) {
		result = GetLastError();
	}
	if (unlikely(CloseHandle(writer->writeHandle) == 0 && result == 0)) {
		result = GetLastError();
	}
	if (unlikely(CloseHandle(writer->readHandle) == 0 && result == 0)) {
		result = GetLastError();
	}
	free(writer);
	return result;
}

#endif
