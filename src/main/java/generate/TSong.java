package generate;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_song
 * @author 
 */
@Data
public class TSong implements Serializable {
    private String id;

    private String songid;

    private String name;

    private String artistid;

    private String lyricid;

    private String mp3url;

    private String origin;

    private Integer num;

    private Date createtime;

    private Date modifytime;

    private static final long serialVersionUID = 1L;
}