@Grab(group='io.github.egonw.bacting', module='managers-cdk', version='0.0.5')
@Grab(group='io.github.egonw.bacting', module='managers-ui', version='0.0.5')
@Grab(group='io.github.egonw.bacting', module='managers-opsin', version='0.0.5')
@Grab(group='io.github.egonw.bacting', module='managers-inchi', version='0.0.5')
@Grab(group='io.github.egonw.bacting', module='managers-chemspider', version='0.0.5')

workspaceRoot = ".."
bioclipse = new net.bioclipse.managers.BioclipseManager(workspaceRoot);
cdk = new net.bioclipse.managers.CDKManager(workspaceRoot);
ui = new net.bioclipse.managers.UIManager(workspaceRoot);
opsin = new net.bioclipse.managers.OpsinManager(workspaceRoot);
inchi = new net.bioclipse.managers.InChIManager(workspaceRoot);
chemspider = new net.bioclipse.managers.ChemspiderManager(workspaceRoot);

bioclipse.requireVersion("2.8")

species = "human"

// download all GPML files for a species, and save those in /WikiPathways/data/$species

dataMap = bioclipse.fullPath("/WikiPathways/data/$species/")
gpmlFiles = new File(dataMap).listFiles()

logFilename = bioclipse.fullPath("/WikiPathways/report_${species}.txt")
logFile = new File(logFilename)
logFile.setText("")
logFile << "" + new Date() + "\n";

structureList = cdk.createMoleculeList()
gpmlFiles.each { file ->
  def filename = file.name
  def data = new XmlParser().parse(file)
  def metabolites = data.Label.findAll{
    it
  }
  metabolites.each() { node ->
    def nodeID = node.'@GraphId'
    def name = node.'@TextLabel'.trim().replaceAll("\n", " ")
    try {
      def molecule = opsin.parseIUPACName(name)
      structureList.add(molecule)
      def inchiObj = inchi.generate(molecule)
      def inchiVal = inchiObj.getValue()
      def inchiKey = inchiObj.getKey()
      def csid = chemspider.resolve(inchiKey)
      reportLine = "${filename}: node $nodeID -> $name -> $inchiKey -> CSID: $csid";
      logFile << reportLine + "\n" 
      js.say(reportLine)
    } catch (Exception exception) {
      // OK, it was not an IUPAC name, or no InChIKey, or ...
      println exception.message
    }
  }
}
