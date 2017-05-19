/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author char0n
 */
public class Movie implements Serializable {

    private Integer id;
    private String title;
    private String year;
    private String plot;
    private String fullPlot;
    private byte[] coverData;
    private Float rating;
    private Integer votes;
    private String tagline;
    private String runtime;
    private String trivia;
    private String goofs;
    private String awards;
    private String akas;
    private Set<MovieCast> cast          = new HashSet<MovieCast>();
    private Set<MovieProperty> languages = new HashSet<MovieProperty>();
    private Set<MovieProperty> countries = new HashSet<MovieProperty>();
    private Set<MovieProperty> genres    = new HashSet<MovieProperty>();
    private Date created;
    private Date updated;

    public Set<MovieProperty> getCountries() {
        return countries;
    }

    public void setCountries(Set<MovieProperty> countries) {
        this.countries = countries;
    }

    public Set<MovieProperty> getGenres() {
        return genres;
    }

    public void setGenres(Set<MovieProperty> genres) {
        this.genres = genres;
    }

    public Set<MovieProperty> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<MovieProperty> languages) {
        this.languages = languages;
    }

    public String getAkas() {
        return akas;
    }

    public void setAkas(String akas) {
        this.akas = akas;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public Set<MovieCast> getCast() {
        return cast;
    }

    public Set<MoviePerson> getActors() {
        Set<MovieCast> allCast  = this.getCast();
        Set<MoviePerson> actors = new HashSet<MoviePerson>();
        for (MovieCast c : allCast) {
            if (c.getType().equals(MoviePersonType.ACTOR)) {
                actors.add(c.getMoviePerson());
            }
        }
        return actors;
    }

    public Set<MoviePerson> getDirectors() {
        Set<MovieCast> allCast     = this.getCast();
        Set<MoviePerson> directors = new HashSet<MoviePerson>();
        for (MovieCast c: allCast) {
            if (c.getType().equals(MoviePersonType.DIRECTOR)) {
                directors.add(c.getMoviePerson());
            }
        }
        return directors;
    }

    public Set<MoviePerson> getWriters() {
        Set<MovieCast> allCast   = this.getCast();
        Set<MoviePerson> writers   = new HashSet<MoviePerson>();
        for (MovieCast c : allCast) {
            if (c.getType().equals(MoviePersonType.WRITER)) {
                writers.add(c.getMoviePerson());
            }
        }
        return writers;
    }

    public void setCast(Set<MovieCast> cast) {
        this.cast = cast;
    }

    public void addCast(MovieCast cast) {
        this.cast.add(cast);
        cast.setMovie(this);
    }

    public void addLanguage(MovieProperty l) {
        this.languages.add(l);
        l.setMovie(this);
    }

    public void addCountry(MovieProperty c) {
        this.countries.add(c);
        c.setMovie(this);
    }

    public void addGenre(MovieProperty g) {
        this.genres.add(g);
        g.setMovie(this);
    }

    public byte[] getCoverData() {
        return coverData;
    }

    public void setCoverData(byte[] coverData) {
        this.coverData = coverData;
    }

    public String getFullPlot() {
        return fullPlot;
    }

    public void setFullPlot(String fullPlot) {
        this.fullPlot = fullPlot;
    }

    public String getGoofs() {
        return goofs;
    }

    public void setGoofs(String goofs) {
        this.goofs = goofs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrivia() {
        return trivia;
    }

    public void setTrivia(String trivia) {
        this.trivia = trivia;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Movie other = (Movie) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }
        if ((this.year == null) ? (other.year != null) : !this.year.equals(other.year)) {
            return false;
        }
        if ((this.plot == null) ? (other.plot != null) : !this.plot.equals(other.plot)) {
            return false;
        }
        if ((this.fullPlot == null) ? (other.fullPlot != null) : !this.fullPlot.equals(other.fullPlot)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 67 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 67 * hash + (this.year != null ? this.year.hashCode() : 0);
        hash = 67 * hash + (this.plot != null ? this.plot.hashCode() : 0);
        hash = 67 * hash + (this.fullPlot != null ? this.fullPlot.hashCode() : 0);
        return hash;
    }
}
