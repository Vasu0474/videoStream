package com.ask.home.videostream.service;

import static com.ask.home.videostream.constants.ApplicationConstants.ACCEPT_RANGES;
import static com.ask.home.videostream.constants.ApplicationConstants.BYTES;
import static com.ask.home.videostream.constants.ApplicationConstants.CONTENT_LENGTH;
import static com.ask.home.videostream.constants.ApplicationConstants.CONTENT_RANGE;
import static com.ask.home.videostream.constants.ApplicationConstants.CONTENT_TYPE;
import static com.ask.home.videostream.constants.ApplicationConstants.VIDEO;
import static com.ask.home.videostream.constants.ApplicationConstants.VIDEO_CONTENT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class VideoStreamService {

	private final Logger logger = LoggerFactory.getLogger(VideoStreamService.class);

	public ResponseEntity<byte[]> prepareContent() {

		try {
			Path path = Paths.get(VIDEO);
			final Long fileSize = Files.size(path);
			long rangeStart = 0;
			long rangeEnd = fileSize - 1;

			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).header(CONTENT_TYPE, VIDEO_CONTENT + "mp4")
					.header(ACCEPT_RANGES, BYTES).header(CONTENT_LENGTH, String.valueOf(rangeEnd))
					.header(CONTENT_RANGE, BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
					.header(CONTENT_LENGTH, String.valueOf(fileSize))
					.body(readByteRangeNew(path, rangeStart, rangeEnd)); // Read the object and convert it as bytes

		} catch (IOException e) {
			logger.error("Exception while reading the file {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	public byte[] readByteRangeNew(Path filename, long start, long end) throws IOException {

		Path path = filename;
		byte[] data = Files.readAllBytes(path);
		byte[] result = new byte[(int) (end - start) + 1];
		System.arraycopy(data, (int) start, result, 0, (int) (end - start) + 1);
		return result;
	}
}