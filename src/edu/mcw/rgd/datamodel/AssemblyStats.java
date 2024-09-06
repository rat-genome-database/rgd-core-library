package edu.mcw.rgd.datamodel;

public class AssemblyStats extends Map{
   private int mapKey;
   private String  totalSequenceLength ;
   private String totalUngappedLength ;
   private String gapsBetweenScaffolds ;
   private int numberOfScaffolds	;
   private String scaffoldN50 ;
   private String scaffoldL50;
   private int numberOfContigs;
   private String contigN50 ;
   private String contigL50	;
   private int totalNumberOfChromosome;

   public int getMapKey() {
      return mapKey;
   }

   public void setMapKey(int mapKey) {
      this.mapKey = mapKey;
   }

   public String getTotalSequenceLength() {
      return totalSequenceLength;
   }

   public void setTotalSequenceLength(String totalSequenceLength) {
      this.totalSequenceLength = totalSequenceLength;
   }

   public String getTotalUngappedLength() {
      return totalUngappedLength;
   }

   public void setTotalUngappedLength(String totalUngappedLength) {
      this.totalUngappedLength = totalUngappedLength;
   }

   public String getGapsBetweenScaffolds() {
      return gapsBetweenScaffolds;
   }

   public void setGapsBetweenScaffolds(String gapsBetweenScaffolds) {
      this.gapsBetweenScaffolds = gapsBetweenScaffolds;
   }



   public String getScaffoldN50() {
      return scaffoldN50;
   }

   public void setScaffoldN50(String scaffoldN50) {
      this.scaffoldN50 = scaffoldN50;
   }

   public String getScaffoldL50() {
      return scaffoldL50;
   }

   public void setScaffoldL50(String scaffoldL50) {
      this.scaffoldL50 = scaffoldL50;
   }


   public int getNumberOfScaffolds() {
      return numberOfScaffolds;
   }

   public void setNumberOfScaffolds(int numberOfScaffolds) {
      this.numberOfScaffolds = numberOfScaffolds;
   }

   public int getNumberOfContigs() {
      return numberOfContigs;
   }

   public void setNumberOfContigs(int numberOfContigs) {
      this.numberOfContigs = numberOfContigs;
   }

   public String getContigN50() {
      return contigN50;
   }

   public void setContigN50(String contigN50) {
      this.contigN50 = contigN50;
   }

   public String getContigL50() {
      return contigL50;
   }

   public void setContigL50(String contigL50) {
      this.contigL50 = contigL50;
   }

   public int getTotalNumberOfChromosome() {
      return totalNumberOfChromosome;
   }

   public void setTotalNumberOfChromosome(int totalNumberOfChromosome) {
      this.totalNumberOfChromosome = totalNumberOfChromosome;
   }
}
