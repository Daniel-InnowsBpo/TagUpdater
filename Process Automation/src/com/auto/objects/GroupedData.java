package com.auto.objects;

import java.util.Set;

public class GroupedData {

	Set<String> serviceID;
	float paidAmount;
	String CPTs = new String();
	String ClaimIDs = new String();
	String RemarkCode = new String();
	String RemarkCodeDesc = new String();

	public GroupedData(Set<String> groupedClaimIDs2, float groupedpaidAmount, String groupedCPTs,
			String groupedClaimIDs, String groupedRemarkCode, String groupedRemarkCodeDesc) {
		serviceID = groupedClaimIDs2;
		paidAmount = groupedpaidAmount;
		CPTs = groupedCPTs;
		ClaimIDs = groupedClaimIDs;
		RemarkCode = groupedRemarkCode;
		RemarkCodeDesc = groupedRemarkCodeDesc;
	}

}
