package net.junfeng.hackexercise;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class GradeRoundingExercise {
	private static final Scanner scan = new Scanner(System.in);
	private static final int ROUNDING_BASE = 5;
	private static final int ROUNDING_BOUNDARY = 3;

	/*
	 * Complete the gradingStudents function below.
	 */
	static int[] gradingStudents(int[] grades) {
		/*
		 * Write your code here.
		 */

		return Arrays.stream(grades).filter(p -> p >= 0).map(GradeRoundingExercise::convert).toArray();

	}

	static int convert(int grade) {
		int result = -1;

		int remainder = grade % ROUNDING_BASE;
		boolean roundingUp = ROUNDING_BASE - remainder < ROUNDING_BOUNDARY;

		if (grade >= 38 && roundingUp) {
			result = grade - remainder + ROUNDING_BASE;
		} else {
			result = grade;
		}

		return result;
	}

	public static void main(String[] args) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

		int n = Integer.parseInt(scan.nextLine().trim());

		int[] grades = new int[n];

		for (int gradesItr = 0; gradesItr < n; gradesItr++) {
			int gradesItem = Integer.parseInt(scan.nextLine().trim());
			grades[gradesItr] = gradesItem;
		}

		int[] result = gradingStudents(grades);

		for (int resultItr = 0; resultItr < result.length; resultItr++) {
			bw.write(String.valueOf(result[resultItr]));

			if (resultItr != result.length - 1) {
				bw.write("\n");
			}
		}

		bw.newLine();

		bw.close();
	}

}
