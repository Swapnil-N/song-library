package app;

public class Song implements Comparable<Song>{
	
	String name;
	String artist;
	String album;
	String year;
	
	public Song(String name, String artist, String album, String year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
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

	@Override
	public int compareTo(Song o) {
		// TODO Auto-generated method stub
		if (name.compareTo(o.getName()) < 0)
			return -1;
		if (name.compareTo(o.getName()) > 0)
			return 1;
		if (artist.compareTo(o.getName()) < 0)
			return -1;
		if (artist.compareTo(o.getName()) > 0)
			return 1;
		return 0;
	}
}
