package ch.so.agi.ilicache;

public class CloneService {

    
    // :/Users/stefan/tmp/ilicache/ java.io.tmpdir
    //String property = "java.io.tmpdir";
    //String tempDir = System.getProperty(property);
    // .iliclone?
    
    // stage. falls erfolgreich, kopieren nach live.
    // immer einzeln, da früher oder später man eventuell nicht alle gleichzeitig clonen will
    // ein originäres ilimodels.xml (resp. ilisite.xml) zeigt dann auf die cloned repos. Müssen die Klone auf das Mutter-Repo zeigen?
    // -> mich dünkt nicht zwingend. Nur wenn ich bei einem einzelnen Clone mit Suchen beginnen würde.
    // Ah, nicht sicher, ob ein ilisite.xml zwingend ein ilimodels benötigt. Falls nicht, ginge es
    // elegant. Es gäbe nur subsidiary sites oder peer sites. Plus noch weitere (z.B. unsesr live geo.so.ch/models).
    // notfalls ein leeres ilimodels.xml
    
    // Ausprobieren (nicht viel verloren)
    // 1. ilisite.xml herstellen per Code
    // 2. ein Repo clonen und files vom Filesystem verfügbar machen.
    
    

}
