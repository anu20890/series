package com.series;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeriesManager {
	public static void main(String[] args) {
		String path = null;
		if (args.length == 0) {
			System.err.println("Please pass path");
			System.exit(0);
		} else {
			path = args[0];
		}
		File[] list = new File(path).listFiles();
		List<File> videoFiles = new ArrayList<File>();
		List<File> srtFiles = new ArrayList<File>();
		File[] arrayOfFile1;
		int j = (arrayOfFile1 = list).length;
		for (int i = 0; i < j; i++) {
			File file = arrayOfFile1[i];
			if (file.isFile()) {
				if (file.getName().endsWith(".srt")) {
					srtFiles.add(file);
				} else {
					videoFiles.add(file);
				}
			}
		}
		for (File videoFile : videoFiles) {
			File srtFile = Utils.getAssociatedSrtFile(srtFiles, videoFile);
			if (srtFile != null) {
				Episode episode = new Episode(videoFile, srtFile);
				try {
					episode.rename();
				} catch (IOException e) {
					System.out.println("Failed to rename");
					e.printStackTrace();
				}
			}
		}
	}
}
