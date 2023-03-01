package edu.mcw.rgd.datamodel;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Sep 24, 2009
 * Time: 1:43:10 PM
 */
public class ConservationScore {

    private String nuc;    // Reference Nuc
    private String chromosome;
    private long position;
    private BigDecimal score;
    private String scoreRating;   // Set with internal logic when score is set.
    private int scoreRatingId = -1;

    public String getNuc() {
        return nuc;
    }

    public void setNuc(String nuc) {
        this.nuc = nuc;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        if (score == null) score = BigDecimal.valueOf(-1.00);
        this.score = score;
        if ((score.compareTo(new BigDecimal("-1.00")) == 0)) {
            this.scoreRating = "Not available";
            this.scoreRatingId = 0;
        } else if (score.compareTo(new BigDecimal("0")) == 0) {
            this.scoreRating = "No conservation";
            this.scoreRatingId = 1;
        }else if (score.compareTo(new BigDecimal(".5")) < 0) {
            this.scoreRating = "Low sequence conservation";
            this.scoreRatingId = 2;
        } else if (score.compareTo(new BigDecimal(".75")) < 0) {
            this.scoreRating = "Moderate sequence conservation";
            this.scoreRatingId = 3;
        } else {
            this.scoreRating = "High sequence conservation";
            this.scoreRatingId = 4;
        }


    }

    public String getScoreRating() {
        return scoreRating;
    }

    public void setScoreRating(String scoreRating) {
        this.scoreRating = scoreRating;
    }

    public int getScoreRatingId() {
        return scoreRatingId;
    }

    public void setScoreRatingId(int scoreRatingId) {
        this.scoreRatingId = scoreRatingId;
    }
}
