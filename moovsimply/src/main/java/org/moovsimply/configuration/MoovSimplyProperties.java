package org.moovsimply.configuration;

import java.io.Serializable;


public class MoovSimplyProperties implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean nodeEmbedded = true; 
	private String[] nodeAdresses = {"localhost:9300", "localhost:9201"}; 
	private String clusterName = "moovsimply"; 
	private String pathData = "moovsimply/esdata";
	
	/**
	 * @return the nodeEmbedded
	 */
	public boolean isNodeEmbedded() {
		return nodeEmbedded;
	}
	/**
	 * @param nodeEmbedded the nodeEmbedded to set
	 */
	public void setNodeEmbedded(boolean nodeEmbedded) {
		this.nodeEmbedded = nodeEmbedded;
	}
	/**
	 * @return the nodeAdresses
	 */
	public String[] getNodeAdresses() {
		return nodeAdresses;
	}
	/**
	 * @param nodeAdresses the nodeAdresses to set
	 */
	public void setNodeAdresses(String[] nodeAdresses) {
		this.nodeAdresses = nodeAdresses;
	}
	/**
	 * @return the clusterName
	 */
	public String getClusterName() {
		return clusterName;
	}
	/**
	 * @param clusterName the clusterName to set
	 */
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	/**
	 * @return the pathData
	 */
	public String getPathData() {
		return pathData;
	}
	/**
	 * @param pathData the pathData to set
	 */
	public void setPathData(String pathData) {
		this.pathData = pathData;
	}
	
//	@Override
//	public String toString() {
//		return StringTools.toString(this);
//	}
	
}
