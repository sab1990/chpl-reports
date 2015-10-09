package chapelPdf;

import com.sun.media.sound.InvalidFormatException;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);
	private static EntityManager em;

	public static void main(String[] args) {
		//Create the entity manager
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ChapelPdfPersistenceUnit");
		em = emf.createEntityManager();

		//Query date from the database
		List<ChapelDataEntity> files = em.createNativeQuery("select c.chapel_id, c.report_url " +
				"from chapel_pdf.chapel_data c " +
				"LEFT JOIN chapel_pdf.chapel_pdf_store p on p.chapel_id = c.chapel_id and p.report_url = c.report_url " +
				"where c.report_url is not null " +
				"and p.chapel_id is null " +
				"order by c.chapel_id", ChapelDataEntity.class).setMaxResults(2).getResultList();

		//Process every file found in the database
		for (ChapelDataEntity file : files) {
			log.info("opening connection");
			log.info(file.getChapelId() + " - " + file.getReportUrl());

			try {
				byte[] pdfBytes = fetchPdf(file.getReportUrl());

				em.getTransaction().begin();

				StoreKey key = new StoreKey();
				key.setChapelId(file.getChapelId());
				key.setReportUrl(file.getReportUrl());

				PdfStore ins = new PdfStore();
				ins.setStoreKey(key);
				ins.setReportStore(pdfBytes);

				em.persist(ins);
				em.getTransaction().commit();
			}
			catch (Exception e) {
				log.error(file.getChapelId() + " - " + file.getReportUrl() + ": " + e);
			}
		}
		log.info("Closing application");
	}

	private static byte[] fetchPdf(String urlString) throws IOException {
		byte[] pdfBytes = null;

		//Load report URL
		URL url = new URL(urlString);

		//Determine content type for file
		String contentType = url.openConnection().getContentType();

		//Process only if file type is pdf
		if (contentType.equalsIgnoreCase("application/pdf")) {
			InputStream in = url.openStream();
			pdfBytes = IOUtils.toByteArray(in);
			in.close();
			return pdfBytes;
		} else if (url.toString().contains("cchit.box.com")) {
			return fetchPdf(getDownloadableBoxUrl(url));
		} else {
			throw new InvalidFormatException();
		}
	}

	private static String getDownloadableBoxUrl(URL url) throws IOException {
		Document doc = Jsoup.connect(url.toString()).get();
		Element link = doc.select("div.mtl").first().select("a").first();
		String href = link.attr("href");
		System.out.println(href);

		Matcher shared_name_matcher = Pattern.compile("/s/(.*)").matcher(url.toString());
		String shared_name = null;
		if (shared_name_matcher.find()) {
			shared_name = shared_name_matcher.group(1);
		}

		Matcher file_id_matcher = Pattern.compile("/file/(.*?)/").matcher(href);
		String file_id = null;
		if (file_id_matcher.find()) {
			file_id = file_id_matcher.group(1);
		}

		return String.format("https://cchit.app.box.com/index.php?rm=box_download_shared_file&shared_name=%1$s&file_id=f_%2$s", shared_name, file_id);
	}

}