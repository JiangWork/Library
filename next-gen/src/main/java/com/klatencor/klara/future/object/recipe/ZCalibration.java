package com.klatencor.klara.future.object.recipe;

import java.util.List;

public class ZCalibration {
	private String method;
	private String swathingView;
	private String swathYSpacing;
	private String cFView;
	private String measured;
	private String expandedBorderView;
	private String expandedBorderType;
	private Border border;
	
	public String getMethod(){
		return this.method;
	}
	
	public void setMethod(String method){
		this.method = method;
	}
	
	public String getSwathingView(){
		return this.swathingView;
	}
	
	public void setSwathingView(String swathingView){
		this.swathingView = swathingView;
	}
	
	public String getSwathYSpacing(){
		return this.swathYSpacing;
	}
	
	public void setSwathYSpacing(String swathYSpacing){
		this.swathYSpacing = swathYSpacing;
	}
	
	public String getCFView(){
		return this.cFView;
	}
	
	public void setCFView(String cFView){
		this.cFView = cFView;
	}
	
	public String getMeasured(){
		return this.measured;
	}
	
	public void setMeasured(String measured){
		this.measured = measured;
	}
	
	public String getExpandedBorderView(){
		return this.expandedBorderView;
	}
	
	public void setExpandedBorderView(String expandedBorderView){
		this.expandedBorderView = expandedBorderView;
	}
	
	public String getExpandedBorderType(){
		return this.expandedBorderType;
	}
	
	public void setExpandedBorderType(String expandedBorderType){
		this.expandedBorderType = expandedBorderType;
	}
	
	public Border getBorder(){
		return this.border;
	}
	
	public void setBorder(Border border){
		this.border = border;
	}
	
	static public class Border{
		private String type;
		private List<BorderPoint> borderPoints;
		
		public String getType(){
			return this.type;
		}
		
		public void setType(String type){
			
			this.type = type;
		}
		
		public List<BorderPoint> getBorderPoints(){
			return this.borderPoints;
		}
		
		public void setBorderPoints(List<BorderPoint> borderPoints){
			this.borderPoints = borderPoints;
		}
	}
	
	static public class BorderPoint{
		private String location;
		private String x;
		private String y;
		
		public String getLocation(){
			return this.location;
		}
		
		public void setLocation(String location){
			this.location = location;
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
