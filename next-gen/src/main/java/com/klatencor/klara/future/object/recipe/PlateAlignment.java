package com.klatencor.klara.future.object.recipe;

import java.util.List;

public class PlateAlignment {
	private String inspectionMethod;
	private String acquisitionView;
	private AlignmentPointInfo alignmentPointInfo;
	private List<AlignmentPoint> alignmentPoints;
	private DatabaseAlignmentPointInfo databaseAlignmentPointInfo;
	private List<DatabaseAlignmentPoint> DatabaseAlignmentPoints;
	
	public String getInspectionMethod(){
		return this.inspectionMethod;
	}
	
	public void setInspectionMethod(String inspectionMethod){
		this.inspectionMethod = inspectionMethod;
	}
	
	public String getAcquisitionView(){
		return this.acquisitionView;
	}
	
	public void setAcquisitionView(String acquisitionView){
		this.acquisitionView = acquisitionView;
	}
	
	public AlignmentPointInfo getAlignmentPointInfo(){
		return this.alignmentPointInfo;
	}
	
	public void setAlignmentPointInfo(AlignmentPointInfo alignmentPointInfo){
		this.alignmentPointInfo = alignmentPointInfo;
	}
	
	public List<AlignmentPoint> getAlignmentPoints(){
		return this.alignmentPoints;
	}
	
	public void setAlignmentPoints(List<AlignmentPoint> alignmentPoints){
		this.alignmentPoints = alignmentPoints;
	}
	
	public DatabaseAlignmentPointInfo getDatabaseAlignmentPointInfo(){
		return this.databaseAlignmentPointInfo;
	}
	
	public void setDatabaseAlignmentPointInfo(DatabaseAlignmentPointInfo databaseAlignmentPointInfo){
		this.databaseAlignmentPointInfo = databaseAlignmentPointInfo;
	}
	
	public List<DatabaseAlignmentPoint> getDatabaseAlignmentPoints(){
		return this.DatabaseAlignmentPoints;
	}
	
	public void setDatabaseAlignmentPoints(List<DatabaseAlignmentPoint> databaseAlignmentPoints){
		this.DatabaseAlignmentPoints = databaseAlignmentPoints;
	}
	
	static public class AlignmentPointInfo{
		private String lightMode;
		private String magnification;
		private String alignmentType;
		
		public String getLightMode(){
			return this.lightMode;
		}
		
		public void setLightMode(String lightMode){
			this.lightMode = lightMode;
		}
		
		public String getMagnification(){
			return this.magnification;
		}
		
		public void setMagnification(String magnification){
			this.magnification = magnification;
		}
		
		public String getAlignmentType(){
			return this.alignmentType;
		}
		
		public void setAlignmentType(String alignmentType){
			this.alignmentType = alignmentType;
		}
	}
	
	static public class AlignmentPoint{
		private String type;
		private String x;
		private String y;
		
		public String getType(){
			return this.type;
		}
		
		public void setType(String type){
			this.type = type;
		}
		
		public String getX(){
			return this.x;
		}
		
		public void setX(String x){
			this.x = x;
		}
		
		public String getY(){
			return this.y;
		}
		
		public void setY(String y){
			this.y = y;
		}
	}
	
	static public class DatabaseAlignmentPointInfo{
		private String lightMode;
		private String magnification;
		private String alignmentType;
		
		public String getLightMode(){
			return this.lightMode;
		}
		
		public void setLightMode(String lightMode){
			this.lightMode = lightMode;
		}
		
		public String getMagnification(){
			return this.magnification;
		}
		
		public void setMagnification(String magnification){
			this.magnification = magnification;
		}
		
		public String getAlignmentType(){
			return this.alignmentType;
		}
		
		public void setAlignmentType(String alignmentType){
			this.alignmentType = alignmentType;
		}
	}
	
	static public class DatabaseAlignmentPoint{
		private String type;
		private String x;
		private String y;
		
		public String getType(){
			return this.type;
		}
		
		public void setType(String type){
			this.type = type;
		}
		
		public String getX(){
			return this.x;
		}
		
		public void setX(String x){
			this.x = x;
		}
		
		public String getY(){
			return this.y;
		}
		
		public void setY(String y){
			this.y = y;
		}
	}
}
