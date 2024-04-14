public class AlignmentResult {
    int score;
    String alignedSeq1;
    String alignedSeq2;
    int matchCount;
    int alignmentLength;
    int gapCountSeq1;
    int gapCountSeq2;

    AlignmentResult(int score, String alignedSeq1, String alignedSeq2, int matchCount, int alignmentLength, int gapCountSeq1, int gapCountSeq2) {
        this.score = score;
        this.alignedSeq1 = alignedSeq1;
        this.alignedSeq2 = alignedSeq2;
        this.matchCount = matchCount;
        this.alignmentLength = alignmentLength;
        this.gapCountSeq1 = gapCountSeq1;
        this.gapCountSeq2 = gapCountSeq2;


    }
    public static AlignmentResult traceback(int[][] matrix, String seq1, String seq2, int match, int mismatch, int gap) {
        int m = seq1.length();
        int n = seq2.length();
        int maxI = 0, maxJ = 0;
        int maxScore = 0;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (matrix[i][j] > maxScore) {
                    maxScore = matrix[i][j];
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        StringBuilder alignmentSeq1 = new StringBuilder();
        StringBuilder alignmentSeq2 = new StringBuilder();
        int matchCount = 0;
        int alignmentLength = 0;
        int gapCountSeq1 =0;
        int gapCountSeq2=0;

        int i = maxI;
        int j = maxJ;
        while (i > 0 && j > 0 && matrix[i][j] > 0) {
            if (seq1.charAt(i-1) == seq2.charAt(j-1)) {
                alignmentSeq1.insert(0, seq1.charAt(i-1));
                alignmentSeq2.insert(0, seq2.charAt(j-1));
                matchCount++;
                i--;
                j--;
            } else if (matrix[i][j] == matrix[i-1][j] + gap) {
                alignmentSeq1.insert(0, seq1.charAt(i-1));
                alignmentSeq2.insert(0, '-');
                gapCountSeq2++;
                i--;
            } else if (matrix[i][j] == matrix[i][j-1] + gap) {
                alignmentSeq1.insert(0, '-');
                alignmentSeq2.insert(0, seq2.charAt(j-1));
                j--;
            } else {
                alignmentSeq1.insert(0, seq1.charAt(i-1));
                alignmentSeq2.insert(0, seq2.charAt(j-1));
                gapCountSeq1++;
                i--;
                j--;
            }
            alignmentLength++;
        }

        return new AlignmentResult(maxScore, alignmentSeq1.toString(), alignmentSeq2.toString(), matchCount, alignmentLength, gapCountSeq1, gapCountSeq2);
    }
}