package fr.insee.coleman.api.domain;

/**
 * AvancementRow
 */
public class RowProgress {
    
    private int total;

	private String status;

	private int batchNum;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(int batchNum) {
		this.batchNum = batchNum;
	}
}