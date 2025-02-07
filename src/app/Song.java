//Swapnil Napuri and Srinandini Marpaka

package app;

public class Song implements Comparable<Song> {

	String name;
	String artist;
	String album;
	String year;

	public Song(String name, String artist, String album, String year) {
		this.name = name.trim();
		this.artist = artist.trim();
		this.album = album.trim();
		this.year = year.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getDisplayString() {
		return name + " | " + artist;
	}

	public String getTextFileString() {
		return name + " |" + artist + " |" + album + " |" + year + " |\n";
	}

	@Override
	public int compareTo(Song o) {
		if (name.toLowerCase().compareTo(o.getName().toLowerCase()) < 0)
			return -1;
		if (name.toLowerCase().compareTo(o.getName().toLowerCase()) > 0)
			return 1;
		if (artist.toLowerCase().compareTo(o.getArtist().toLowerCase()) < 0)
			return -1;
		if (artist.toLowerCase().compareTo(o.getArtist().toLowerCase()) > 0)
			return 1;
		return 0;
	}
}
