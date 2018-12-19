package net.junfeng.hackexercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

public class TyroChallenge2GetMovies {

	/*
	 * Complete the function below.
	 */
	static String[] getMovieTitles(String substr) {
		if (substr == null || substr.isEmpty()) {
			return null;
		}

		List<String> titles = new ArrayList<>();

		String requestUrl = Constants.MOVIE_SEARCH_URL + Constants.TITLE_PARAMETER + substr;
		String responseStr = fetchContent(requestUrl);
		SearchResponse response = parseResponse(responseStr, SearchResponse.class);

		if (isValidResponse(response)) {
			response.getMovies().stream().forEach(movie -> titles.add(movie.getTitle()));

			int currentPage = Integer.valueOf(response.getPage());
			int totalPage = response.getTotalPages();
			int totalMovies = response.getTotal();

			while (currentPage < totalPage && titles.size() < totalMovies) {
				requestUrl = Constants.MOVIE_SEARCH_URL + Constants.TITLE_PARAMETER + substr + Constants.PAGE_PARAMETER
						+ (++currentPage);
				responseStr = fetchContent(requestUrl);
				response = parseResponse(responseStr, SearchResponse.class);

				if (isValidResponse(response)) {
					response.getMovies().stream().forEach(movie -> titles.add(movie.getTitle()));
				}
			}
		}

		titles.sort((title1, title2) -> title1.compareTo(title2));
		return titles.toArray(new String[0]);
	}

	/**
	 * Parses the input responseStr into the java object as class type T.
	 * 
	 * @param             <T> the type of input clazz
	 * @param responseStr to parse
	 * @param clazz       the corresponding {@link Class} to the input responseStr
	 * @return java object based on the given responseStr
	 */
	private static <T> T parseResponse(String responseStr, Class<T> clazz) {
		Gson gson = new Gson();
		return gson.fromJson(responseStr, clazz);
	}

	/**
	 * Checks if the given {@link SearchResponse} is valid.
	 * 
	 * @param response to check
	 * @return {@code true} if it is valid; otherwise {@code false}
	 */
	private static boolean isValidResponse(SearchResponse response) {
		return response != null && response.getTotal() > 0;
	}

	/**
	 * Fetches content using the given urlStr.
	 * 
	 * @param urlStr the content end-point
	 * @return the content in string format
	 */
	private static String fetchContent(String urlStr) {
		StringBuffer result = new StringBuffer();

		HttpURLConnection connection = null;
		try {

			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(Constants.HTTP_GET);
			connection.setRequestProperty(Constants.ACCEPT, Constants.ACCEPT_VALUE);

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new ConnectException("Failed : HTTP error code : " + connection.getResponseCode());
			}

			try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String inputLine;
				result = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					result.append(inputLine);
				}
			}
		} catch (IOException exception) {
			String errorMsg = String.format("Encountered error [%s] when fetching content.", exception.getMessage());
			throw new RuntimeException(errorMsg, exception);
		} finally {
			connection.disconnect();
		}

		return result.toString();
	}

	/**
	 * Response model is used to parse JSON response.
	 */
	class SearchResponse {
		private String page;

		@com.google.gson.annotations.SerializedName("per_page")
		private int perPage;

		private int total;

		@com.google.gson.annotations.SerializedName("total_pages")
		private int totalPages;

		@com.google.gson.annotations.SerializedName("data")
		private List<Movie> movies;

		String getPage() {
			return page;
		}

		int getPerPage() {
			return perPage;
		}

		int getTotal() {
			return total;
		}

		int getTotalPages() {
			return totalPages;
		}

		List<Movie> getMovies() {
			return movies;
		}
	}

	/**
	 * Movie model is used to parse JSON response.
	 */
	class Movie {
		@com.google.gson.annotations.SerializedName("Poster")
		private String poster;

		@com.google.gson.annotations.SerializedName("Title")
		private String title;

		@com.google.gson.annotations.SerializedName("Type")
		private String type;

		@com.google.gson.annotations.SerializedName("Year")
		private int year;

		private String imdbID;

		String getTitle() {
			return title;
		}
	}

	/**
	 * Store all constants which are used in the solution.
	 */
	abstract class Constants {
		public static final String HTTP_GET = "GET";
		public static final String ACCEPT = "Accept";
		public static final String ACCEPT_VALUE = "application/json";
		public static final String MOVIE_SEARCH_URL = "https://jsonmock.hackerrank.com/api/movies/search";

		public static final String PAGE_PARAMETER = "&page=";
		public static final String TITLE_PARAMETER = "/?Title=";
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Try to type in 'spiderman' or 'waterworld' ...");

		Scanner in = new Scanner(System.in);
		String _substr;
		try {
			_substr = in.nextLine();
		} catch (Exception e) {
			_substr = null;
		}

		String[] res = getMovieTitles(_substr);
		Arrays.stream(res).forEach(System.out::println);
	}
}
