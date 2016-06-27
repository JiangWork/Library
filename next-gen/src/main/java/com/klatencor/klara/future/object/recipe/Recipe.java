package com.klatencor.klara.future.object.recipe;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.klatencor.klara.future.object.Version;
import com.klatencor.klara.future.utils.IOUtils;

/**
 * A recipe java bean which is identical to XML.
 *
 * @author jiangzhao
 * @date May 28, 2016
 * @version V1.0
 */
public class Recipe {

	private GeneralRecipeData recipeGeneral;
	private Behavior behavior;
	private PlateAlignment plateAlignment;
	private ZCalibration zCalibration;
	private List<InspectionArea> inspectionAreaList;
	
	/**Additional fields**/
	private String recipeStoragePath;
	private String recipeType; // S or N
	private String systemName;  // i.e., hostName
	private int fmId;
	private int ppId;
	private int machineType;
	
	//set by readHeader
	private int recipeVersion;
	private int rcpStoredSeconds;
	private int rcpStoredMicroSeconds;
	private int rcpStoredDisplayZone;
	private int rcpStoredGmtOffset;
	private int plateType;
	private int plateSize;
	private int plateThickness;
	private int pellicleHeight;
	private short systemType;
	
	/**
	 * Read the very beginning information of a Recipe.
     * Usually, such information in Recipe file is in the fixed
     * and unchanged position, without the impact of version evolution. 
     * 
     * Often, such information is not in the XML file.
	 * @param recipePath
	 * @throws IOException
	 */
	public void readHeader(String recipePath) throws IOException {
		FileInputStream fis;			
		fis = new FileInputStream(recipePath);
		BufferedInputStream bis = new BufferedInputStream(fis);
		DataInputStream dis = new DataInputStream(bis);
		
		int magicCookie = dis.readInt();
		recipeVersion = dis.readInt();
		if (recipeVersion >= Version.V210
				&& recipeVersion < Version.V600) {
			setMachineType(5);  // FIVEXX
		}
		if (recipeVersion >= Version.V600) {
			setMachineType(7);  // SIX_XX
		}
		dis.readInt(); 
		dis.readInt();  // kla9XStatus1
		dis.readInt(); //kla9XStatus2
		if (recipeVersion >= Version.V206) {
			IOUtils.readString(dis);
			systemName = IOUtils.readString(dis);
			magicCookie = dis.readInt();  // Date magic
			if (1936483444 == magicCookie ) {
				dis.readInt();  // version
				rcpStoredSeconds      = dis.readInt();
				rcpStoredMicroSeconds = dis.readInt();
				rcpStoredDisplayZone  = dis.readInt();
				rcpStoredGmtOffset    = dis.readInt();
			} else {
				rcpStoredSeconds = magicCookie;
				rcpStoredMicroSeconds = dis.readInt();
			}
		}
		IOUtils.readString(dis); // Recipe Name
		IOUtils.readString(dis); // Note

		plateType = dis.readInt();
		if (recipeVersion >= Version.V230) {
			IOUtils.readString(dis); // Plate type
		}
		plateSize = dis.readInt();
		if (plateSize > 1) {
			plateSize = 1;
		}
		if (recipeVersion >= Version.V100) {
			plateThickness = dis.readInt();
			pellicleHeight = dis.readInt();
		}
		if (recipeVersion >= Version.V651) {
			IOUtils.readString(dis); // barcode
			IOUtils.readString(dis); // barcodeLoc
		}
		if (recipeVersion >= Version.V762) {
			IOUtils.readString(dis); // parameters
		}
		if (recipeVersion >= Version.V600) {
			systemType = 9;   //SIX_XX_SYSTEM_TYPE
		} else if (recipeVersion >= Version.V101
				&& recipeVersion < Version.V600) {
			systemType = (short) dis.readInt(); 
		} else {			
			systemType = 0;  
		}
		dis.close();
	}
	
	public short getSystemType(){
		return this.systemType;
	}
	
	public int getPlateType(){
		return plateType;
	}
	
	public int getPlateSize(){
		return plateSize;
	}
	
	public int getRecipeVersion(){
		return recipeVersion;
	}
	
	public int getRcpStoredSeconds(){
		return rcpStoredSeconds;
	}
	
	public int getRcpStoredMicroSeconds() {
		return rcpStoredMicroSeconds;
	}
	
	public int getRcpStoredDisplayZone() {
		return rcpStoredDisplayZone;
	}
	
	public int getRcpStoredGmtOffset() {
		return rcpStoredGmtOffset;
	}
	
	public void setMachineType(int machineType){
		this.machineType = machineType;
	}
	
	public int getMachineType(){
		return this.machineType;
	}
	
	public GeneralRecipeData getRecipeGeneral() {
		return recipeGeneral;
	}
	public void setRecipeGeneral(GeneralRecipeData recipeGeneral) {
		this.recipeGeneral = recipeGeneral;
	}
	public Behavior getBehavior() {
		return behavior;
	}
	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}
	public PlateAlignment getPlateAlignment() {
		return plateAlignment;
	}
	public void setPlateAlignment(PlateAlignment plateAlignment) {
		this.plateAlignment = plateAlignment;
	}
	public ZCalibration getZCalibration() {
		return zCalibration;
	}
	public void setZCalibration(ZCalibration zCalibration) {
		this.zCalibration = zCalibration;
	}
	public List<InspectionArea> getInspectionAreaList() {
		if(inspectionAreaList == null){
			inspectionAreaList = new ArrayList<InspectionArea>();
		}
		return inspectionAreaList;
	}
	public void setInspectionAreaList(List<InspectionArea> inspectionAreaList) {
		this.inspectionAreaList = inspectionAreaList;
	}
	public String getRecipeStoragePath() {
		return recipeStoragePath;
	}
	public void setRecipeStoragePath(String recipeStoragePath) {
		this.recipeStoragePath = recipeStoragePath;
	}
	public String getRecipeType() {
		return recipeType;
	}
	public void setRecipeType(String recipeType) {
		this.recipeType = recipeType;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public int getFmId() {
		return fmId;
	}
	public void setFmId(int fmId) {
		this.fmId = fmId;
	}
	public int getPpId() {
		return ppId;
	}
	public void setPpId(int ppId) {
		this.ppId = ppId;
	}
	
	
}
