public class SmithWaterman {
    public static int[][] smithWaterman(String seq1, String seq2, int match, int mismatch, int gap) {
        int m = seq1.length();
        int n = seq2.length();
        int[][] score = new int[m+1][n+1];

        for (int i = 0; i <= m; i++) {
            score[i][0] = 0;
        }

        for (int j = 0; j <= n; j++) {
            score[0][j] = 0;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int matchScore = seq1.charAt(i-1) == seq2.charAt(j-1) ? match : mismatch;
                int scoreDiagonal = score[i-1][j-1] + matchScore;
                int scoreUp = score[i-1][j] + gap;
                int scoreLeft = score[i][j-1] + gap;
                score[i][j] = Math.max(0, Math.max(scoreDiagonal, Math.max(scoreUp, scoreLeft)));
            }
        }
        return score;
    }



    public static void printMatrix(int[][] matrix, String seq1, String seq2) {
        System.out.print("     ");
        for (int j = 0; j < seq2.length(); j++) {
            System.out.print(seq2.charAt(j) + "  ");
        }
        System.out.println();
        for (int i = 0; i <= seq1.length(); i++) {
            if (i > 0) {
                System.out.print(seq1.charAt(i-1) + " ");
            } else {
                System.out.print("  ");
            }
            for (int j = 0; j <= seq2.length(); j++) {
                System.out.printf("%2d ", matrix[i][j]);
            }
            System.out.println();
        }
    }
}
