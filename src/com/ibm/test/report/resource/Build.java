package com.ibm.test.report.resource;

import com.ibm.json.java.JSONObject;


/** Define application build
 * 
 * @author ruifengm
 * @since 2016-Jul-28
 */

public class Build {
	private String buildName; 
	private String buildVersion; 
	private String buildTimeStamp;
	private String gitBranch; 
	private String sprint;
	
	
	// constructor
	public Build() {
		super();
	}
	
	public Build(String buildName, String buildVersion, String buildTimeStamp,
			String gitBranch, String sprint) {
		super();
		this.buildName = buildName;
		this.buildVersion = buildVersion;
		this.buildTimeStamp = buildTimeStamp;
		this.gitBranch = gitBranch;
		this.sprint = sprint;
	}
	
	// to JSON
	public JSONObject toJSON() {
		JSONObject returnObj = new JSONObject(); 
		
		returnObj.put("BuildName", this.buildName);
		returnObj.put("BuildVersion", this.buildVersion);
		returnObj.put("BuildTimestamp", this.buildTimeStamp);
		returnObj.put("GitBranch", this.gitBranch);
		returnObj.put("Sprint", this.sprint);
		
		return returnObj;
	}

	// getters and setters
	
	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public String getBuildVersion() {
		return buildVersion;
	}

	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}

	public String getBuildTimeStamp() {
		return buildTimeStamp;
	}

	public void setBuildTimeStamp(String buildTimeStamp) {
		this.buildTimeStamp = buildTimeStamp;
	}

	public String getGitBranch() {
		return gitBranch;
	}

	public void setGitBranch(String gitBranch) {
		this.gitBranch = gitBranch;
	}

	public String getSprint() {
		return sprint;
	}

	public void setSprint(String sprint) {
		this.sprint = sprint;
	} 

}
