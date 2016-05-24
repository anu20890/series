package com.series;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiSeriesManeger {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String path = null;
		if (args.length == 0) {
			System.err.println("Please pass path");
			System.exit(0);
		} else {
			path = args[0];
		}
		String regex = "(.+)[\\.\\s][S|s](\\d{1,2}[e|E]\\d{1,2})[\\.\\s_].+";
		Pattern pattern = Pattern.compile(regex);
		Files.walk(new File(path).toPath()).filter(file -> file.toFile().isFile()).forEach(file -> {
			String old_name = file.getFileName().toString();
			Matcher matcher = pattern.matcher(old_name);
			if (matcher.find()) {

				String series = matcher.group(1);
				String[] info = matcher.group(2).split("[e|E]");
				String season = info[0];
				Integer episode = Integer.parseInt(info[1]);

				try {
					String episode_name = ApiSeriesManeger.getSeriesName(series, season).get(episode);
					String new_name = old_name.replace(matcher.group(2), matcher.group(2) + "." + episode_name);
					// new_name = matcher.replaceFirst(matcher.group(2) + "." + episode_name);
				System.out.println(old_name + "====>" + new_name);
				if (episode_name != null) {
					// Files.move(file, file.resolveSibling(new_name), new CopyOption[0]);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// System.out.println(old_name);
				// System.out.println(matcher.group(1));
				// System.out.println(matcher.group(2));
				// System.out.println(info[0]);
				// System.out.println(info[1]);
			}
		}	);
	}

	private static Map<String, Map<Integer, String>> cache = new HashMap<String, Map<Integer, String>>();

	private static Map<Integer, String> getSeriesName(String series, String season) throws JSONException, MalformedURLException,
	IOException {
		String cache_key = series + season;
		if (!cache.containsKey(cache_key)) {
			Map<Integer, String> map = new HashMap<Integer, String>();
			String url = String.format("http://www.omdbapi.com/?t=%s&Season=%s", series.replaceAll("[\\.\\s]", "%20"), season);
			System.out.println(url);
			JSONObject json = new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
			json.getJSONArray("Episodes").forEach(obj -> {
				Integer key = Integer.parseInt(((JSONObject) obj).getString("Episode"));
				String value = ((JSONObject) obj).getString("Title");
				map.put(key, value);
			});
			cache.put(cache_key, map);
		}
		return cache.get(cache_key);
	}

}
