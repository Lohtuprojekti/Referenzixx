import referenzixx.*
import referenzixx.database.*
import java.util.List
import java.util.ArrayList;
import java.io.File
import org.jbibtex.BibTeXEntry


description "Viitteitä voi filtteröidä yhden hakuehdon perusteella"

scenario "Arto hakee viitteitä vuosiluvun perusteella", {

        File file = new File("src/test/shortbibtexfile.bib")
        DatabaseUtils dbu = new DatabaseUtils(file)
        List<BibTeXEntry> results = new ArrayList<BibTeXEntry>()
    
    when "Arto hakee viitteitä hakusanalla 2016", {
        results = dbu.getReferences("2016")
    }
    
    then "Palautetaan lista viitteitä jotka on julkaistu vuonna 2016", {
        results.size().shouldBe(1)
    }
}

scenario "Yhtään viitettä ei palauteta jos hakuehdot eivät täyty", {
    
        File file = new File("src/test/shortbibtexfile.bib")
        DatabaseUtils dbu = new DatabaseUtils(file)
        List<BibTeXEntry> results = new ArrayList<BibTeXEntry>();
    
    when "Arto hakee viitteitä hakusanalla Cats rule", {
        List<String> filters = new ArrayList<String>()
        filters.add("Cats rule")
        results = dbu.getReferences(filters)
    }

    then "Palautetaan tyhjä lista viitteitä", {
        results.size().shouldBe(0)
    }
}

scenario "Viitteitä voi filtteröidä useiden hakuehtojen perusteella", {
    
        File file = new File("src/test/shortbibtexfile.bib")
        DatabaseUtils dbu = new DatabaseUtils(file)
        List<BibTeXEntry> results = new ArrayList<BibTeXEntry>();
   
    when "Arto hakee viitteitä hakusanoilla 2016 ja joku", {
        String filters = "1628 AND joku"
        results = dbu.getReferences(filters)
    }

    then "Palautetaan lista viitteitä jotka on julkaistu vuonna 2016 ja julkaisija on Joku", {
        results.size().shouldBe(1)
    }
}
    