package chapelPdf;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class StoreKey implements Serializable {
    @Column(name = "chapel_id")
    private String chapelId;

	@Column(name = "report_url")
    private String reportUrl;


    public void setChapelId(String chapelId) {
        this.chapelId = chapelId;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }
}
