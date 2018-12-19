package net.junfeng.hackexercise;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Test {

	private static final String ALL_SKILLS = "pcmbz";

	/*
	 * Complete the function below.
	 */
	static int differentTeams(String skills) {
		if (skills == null || skills.isEmpty()) {
			return 0;
		}

		// convert string to List
		List<Character> skillsList = new LinkedList<>();
		for (int i = 0; i < skills.length(); i++) {
			skillsList.add(skills.charAt(i));
		}

		int numOfTeam = 0;
		while (skillsList.size() >= ALL_SKILLS.length()) {
			StringBuffer teamSkills = new StringBuffer("");

			Iterator<Character> iterator = skillsList.iterator();
			itrLoop: while (iterator.hasNext()) {
				Character skill = iterator.next();
				boolean isSkillMissing = !teamSkills.toString().contains(skill.toString());
				if (teamSkills.length() < ALL_SKILLS.length() && isSkillMissing) {
					teamSkills.append(skill);
					iterator.remove();
				}

				if (isPerfectTeam(teamSkills.toString())) {
					numOfTeam++;
					break itrLoop;
				}
			}
		}

		return numOfTeam;
	}

	/**
	 * Checks if the given teamSkills include all required skills as a perfect team.
	 * 
	 * @param teamSkills to check
	 * @return {@code true} if the given teamSkills include all required skills as a
	 *         perfect team; otherwise {@code false}
	 */
	static boolean isPerfectTeam(String teamSkills) {
		if (teamSkills == null || teamSkills.isEmpty() || teamSkills.length() != ALL_SKILLS.length()) {
			return false;
		}

		// teamSkills must match all skills in ALL_SKILLS constant
		for (int i = 0; i < ALL_SKILLS.length(); i++) {
			String skill = String.valueOf(ALL_SKILLS.charAt(i));
			if (!teamSkills.contains(skill)) {
				return false;
			}

		}

		return true;
	}

	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		final String fileName = System.getenv("OUTPUT_PATH");
		BufferedWriter bw = null;
		if (fileName != null) {
			bw = new BufferedWriter(new FileWriter(fileName));
		} else {
			bw = new BufferedWriter(new OutputStreamWriter(System.out));
		}

		int res;
		String skills;
		try {
			skills = in.nextLine();
		} catch (Exception e) {
			skills = null;
		}

		res = differentTeams(skills);
		bw.write(String.valueOf(res));
		bw.newLine();

		bw.close();
	}

}
