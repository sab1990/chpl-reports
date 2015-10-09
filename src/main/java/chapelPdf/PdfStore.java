package chapelPdf;

import javax.persistence.*;

/**
 * Created by sabalshrestha on 10/6/15.
 */
@Entity
@Table(name = "chapel_pdf_store", schema = "chapel_pdf", catalog = "sabalshrestha")
public class PdfStore {

	@Basic
	@Column(name = "report_store")
	private byte[] reportStore;

    @EmbeddedId
    private StoreKey storeKey;

	public StoreKey getStoreKey() {
		return storeKey;
	}

	public void setStoreKey(StoreKey storeKey) {
		this.storeKey = storeKey;
	}

    public byte[] getReportStore() {
        return reportStore;
    }

    public void setReportStore(byte[] reportStore) {
        this.reportStore = reportStore;
    }
}

