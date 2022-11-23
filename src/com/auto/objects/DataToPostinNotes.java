package com.auto.objects;

public class DataToPostinNotes {

	String groupedServiceID;
	float paidAmnt;
	String claimID;
	String cPTToPost;
	String remarkCodeWithCPT;
	String remarkCodeDesc;
	String remarkCode;

	public DataToPostinNotes(String ServiceIDToPostInNotes, float paidAmount, String claimId, String cPt,
			String remarkCodeWithCPTIn, String remarkCodeDescIn, String remarkCodeIn) {
		groupedServiceID = ServiceIDToPostInNotes;
		paidAmnt = paidAmount;
		claimID = claimId;
		cPTToPost = cPt;
		remarkCodeWithCPT = remarkCodeWithCPTIn.replaceAll(", ", "");
		remarkCodeDesc = remarkCodeDescIn.replaceAll(", ", "\n");
		remarkCode = remarkCodeIn;
	}
}
