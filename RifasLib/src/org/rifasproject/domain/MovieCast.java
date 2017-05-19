/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.domain;

import java.io.Serializable;

/**
 *
 * @author char0n
 */
public class MovieCast implements Serializable {

    private Movie movie;
    private MoviePerson moviePerson;
    private MoviePersonType type;
    private String character;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public MoviePerson getMoviePerson() {
        return moviePerson;
    }

    public void setMoviePerson(MoviePerson moviePerson) {
        this.moviePerson = moviePerson;
    }

    public MoviePersonType getType() {
        return type;
    }

    public void setType(MoviePersonType type) {
        this.type = type;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MovieCast other = (MovieCast) obj;
        if (this.movie != other.movie && (this.movie == null || !this.movie.equals(other.movie))) {
            return false;
        }
        if (this.moviePerson != other.moviePerson && (this.moviePerson == null || !this.moviePerson.equals(other.moviePerson))) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if ((this.character == null) ? (other.character != null) : !this.character.equals(other.character)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + (this.movie != null ? this.movie.hashCode() : 0);
        hash = 61 * hash + (this.moviePerson != null ? this.moviePerson.hashCode() : 0);
        hash = 61 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 61 * hash + (this.character != null ? this.character.hashCode() : 0);
        return hash;
    }
}