/**
 * class that describes the content of the QR code, the signature under the QR code (in this case ICCID eSIM profile) and the file name (extension .png)
 * @author Ivan Emelyanenko
 * @version 1.0
 *
 */

public class PreparationDataQR {
    /**
     * file name with QR code
     */
    public String nameQR;
    /**
     * signature under QR code
     */
    public String signatureQR;
    /**
     * information encoded into the QR code, in this case LPA link for downloading the eSIM profile from the RSP platform.
     */
    public String LPALink;

    /**
     * This is a constructor to initialize PreparationDataQR object (data object for generating QR code)
     * @param lineFromQrdataFile line from file, in this case line from .qrdata file
     */

    public PreparationDataQR(String lineFromQrdataFile){

        setParameters(lineFromQrdataFile);
    }

    /**
     * This method called in the constructor PreparationDataQR class
      * @param dataFromQrdataFile line from file to initialize object PreparationDataQR
     */
    public void setParameters(String dataFromQrdataFile){
        String[] parametersProfile = dataFromQrdataFile.split("\\s");
          if (parametersProfile[0].length()==20){
              this.nameQR = parametersProfile[0].substring(0, parametersProfile[0].length()-1);
              this.signatureQR = parametersProfile[0].substring(0, parametersProfile[0].length()-1);
              this.LPALink = parametersProfile[1];
          }
          else{

              this.nameQR = parametersProfile[0];
              this.signatureQR = parametersProfile[0];
              this.LPALink = parametersProfile[1];


          }

    }
}
