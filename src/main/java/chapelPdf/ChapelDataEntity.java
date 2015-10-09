package chapelPdf;

import javax.persistence.*;

/**
 * Created by sabalshrestha on 10/5/15.
 */
@Entity
@Table(name = "chapel_data", schema = "chapel_pdf", catalog = "sabalshrestha")
public class ChapelDataEntity {
    @Id
    @Column(name = "chapel_id")
    private String chapelId;

	@Basic
	@Column(name = "report_url")
    private String reportUrl;


    public String getChapelId() {
        return chapelId;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChapelDataEntity that = (ChapelDataEntity) o;

        if (chapelId != null ? !chapelId.equals(that.chapelId) : that.chapelId != null) return false;
        if (reportUrl != null ? !reportUrl.equals(that.reportUrl) : that.reportUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chapelId != null ? chapelId.hashCode() : 0;
        result = 31 * result + (reportUrl != null ? reportUrl.hashCode() : 0);
        return result;
    }
}
