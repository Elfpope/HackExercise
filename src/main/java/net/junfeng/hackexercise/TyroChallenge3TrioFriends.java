package net.junfeng.hackexercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TyroChallenge3TrioFriends {

	/* ------- ANSWER START ------- */

	/*
	 * Complete the 'bestTrio' function below.
	 *
	 * The function is expected to return an INTEGER. The function accepts
	 * UNWEIGHTED_INTEGER_GRAPH friends as parameter.
	 */

	/*
	 * For the unweighted graph, <name>:
	 *
	 * 1. The number of nodes is <name>Nodes. 2. The number of edges is <name>Edges.
	 * 3. An edge exists between <name>From[i] and <name>To[i].
	 *
	 */

	private static final String EMPTY_STRING = "";
	private static final int MINIMUM_CONNECTIONS_OF_A_TRIO_NODE = 2;

	public static int bestTrio(int friendsNodes, List<Integer> friendsFrom, List<Integer> friendsTo) {
		boolean trioImpossible = friendsNodes < 3;
		boolean emptyFriendsFrom = friendsFrom == null || friendsFrom.isEmpty();
		boolean emptyFriendsTo = friendsTo == null || friendsTo.isEmpty();
		if (trioImpossible || emptyFriendsFrom || emptyFriendsTo || friendsFrom.size() != friendsTo.size()) {
			return -1;
		}

		List<String> allConnections = new ArrayList<>();
		// The sizes of both friendsFrom and friendsTo must be the SAME at this stage.
		for (int i = 0; i < friendsFrom.size(); i++) {
			String from = String.valueOf(friendsFrom.get(i));
			String to = String.valueOf(friendsTo.get(i));
			String connection = Stream.of(from, to).sorted().collect(Collectors.joining());
			allConnections.add(connection);
		}

		// construct the friendShipMap to store all connections (aka friendship) belong
		// to each node (aka perosn)
		Map<String, List<String>> friendShipMap = new HashMap<>();
		for (int i = 1; i <= friendsNodes; i++) {
			String node = String.valueOf(i);
			List<String> nodeConnections = allConnections.stream().filter(p -> p.contains(node))
					.collect(Collectors.toList());

			friendShipMap.put(node, nodeConnections);
		}

		int result = 0;
		Set<String> trioes = new HashSet<>();

		for (java.util.Map.Entry<String, List<String>> entry : friendShipMap.entrySet()) {
			String node = entry.getKey();
			List<String> nodeConnections = entry.getValue();
			if (!hasMinimumTrioConnection(nodeConnections)) {
				continue;
			}

			for (String conn : nodeConnections) {
				String fiend = conn.replace(node, EMPTY_STRING);

				List<String> fiendConnections = friendShipMap.get(fiend);
				if (!hasMinimumTrioConnection(fiendConnections)) {
					continue;
				}

				for (String con : fiendConnections) {
					String fiendOfFriend = con.replace(fiend, EMPTY_STRING);
					if (fiendOfFriend.equals(node)) {
						continue;
					}

					nodeConnections.stream().filter(p -> p.contains(fiendOfFriend)).findFirst().ifPresent(c -> {
						Set<String> trio = Stream.of(node, fiend, fiendOfFriend).collect(Collectors.toSet());
						trio.stream().sorted((str1, str2) -> str1.compareTo(str2));
						trioes.add(trio.stream().collect(Collectors.joining()));
					});
				}
			}
		}

		System.out.println("friendshipMap: " + friendShipMap.toString());
		System.out.println("trioes: " + trioes.toString());

		if (trioes.isEmpty()) {
			return -1;
		}

		List<Integer> minFriendScores = new ArrayList<>();
		for (String trio : trioes) {
			int minFriendScore = 0;
			for (char node : trio.toCharArray()) {
				List<String> nodeConnections = friendShipMap.get(String.valueOf(node));
				minFriendScore += nodeConnections.stream()
						.filter(con -> TyroChallenge3TrioFriends.outsideTrio(con, trio)).count();
			}

			minFriendScores.add(minFriendScore);
		}

		result = minFriendScores.stream().mapToInt(v -> v).min().orElse(0);

		System.out.println("minFriendScores: " + minFriendScores);

		System.out.println("result: " + result);

		return result;
	}

	/**
	 * Checks if the given nodeConnection is outside of the given trio friendship.
	 * 
	 * @param nodeConnection the two-node connection
	 * @param trio           the tri-node connection
	 * @return {@code true} if the given nodeConnection is outside of the given trio
	 *         friendship; otherwise {@code false}
	 */
	private static boolean outsideTrio(String nodeConnection, String trio) {
		for (char ch : nodeConnection.toCharArray()) {
			String chStr = String.valueOf(ch);
			if (!trio.contains(chStr)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if the given connections has minimum connection to form the trio
	 * relationship.
	 * 
	 * @param connections to check
	 * @return {@code true} if the given connections has minimum connection to form
	 *         the trio relationship; otherwise {@code false}
	 */
	static boolean hasMinimumTrioConnection(List<String> connections) {
		return connections != null && connections.size() >= MINIMUM_CONNECTIONS_OF_A_TRIO_NODE;
	}

	/* --------- ANSWER END --------- */

	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

		String[] friendsNodesEdges = bufferedReader.readLine().replaceAll("\\s+$", EMPTY_STRING).split(" ");

		int friendsNodes = Integer.parseInt(friendsNodesEdges[0]);
		int friendsEdges = Integer.parseInt(friendsNodesEdges[1]);

		List<Integer> friendsFrom = new ArrayList<>();
		List<Integer> friendsTo = new ArrayList<>();

		IntStream.range(0, friendsEdges).forEach(i -> {
			try {
				String[] friendsFromTo = bufferedReader.readLine().replaceAll("\\s+$", EMPTY_STRING).split(" ");

				friendsFrom.add(Integer.parseInt(friendsFromTo[0]));
				friendsTo.add(Integer.parseInt(friendsFromTo[1]));
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});

		int result = bestTrio(friendsNodes, friendsFrom, friendsTo);

		System.out.println("minimum possible friendship sum for any trio: " + result);
	}
}
