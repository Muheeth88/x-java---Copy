package com.xapp.xjava.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.ArrayUtils;

import com.xapp.xjava.entities.Movie;
import com.xapp.xjava.entities.User;
import com.xapp.xjava.models.MovieIdReq;
import com.xapp.xjava.repositories.UsersRepository;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MoviesService moviesService;

    // ------------- create user
    public User createUser(User user) {
        User newUser = usersRepository.save(user);
        return newUser;
    }

    // ----------------- get all users
    public List<User> getAllUsers() {
        List<User> allUsers = usersRepository.findAll();
        return allUsers;
    }

    // ----------------- get user
    public Optional<User> getUser(Long userId) {
        Optional<User> user = usersRepository.findById(userId);
        // UserEntity user = usersRepository.findByUserId();
        return user;
    }

    // public Optional<User> getUser(String userName) {
    // Optional<User> user = usersRepository.findByUserName(userName);
    // return user;
    // }

    public User getUser(String email) {
        User user = usersRepository.findByEmail(email);
        return user;
    }

    public User editUser(Long userId, User user) {
        Optional<User> currentUserOp = getUser(userId);
        if (currentUserOp.isPresent()) {
            User currentUser = currentUserOp.get();
            currentUser.setUserName(user.getUserName());
            currentUser.setDateOfBirth(user.getDateOfBirth());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());
            currentUser.setRole(user.getRole());
            User modifiedUser = createUser(currentUser);
            return modifiedUser;
        }
        return null;

    }

    public User handleWatchList(String userName, MovieIdReq req) throws Exception {
        try {
            User currentUser = getUser(userName);          
            List<Long> watchList = currentUser.getWatchList();
            if(watchList.contains(req.getMovieId())) {
                watchList.remove(req.getMovieId());
            } else {
                watchList.add(req.getMovieId());
            }
            currentUser.setWatchList(watchList);
            User modifiedUser = createUser(currentUser);
            return modifiedUser;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something Went wrong in Service!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return null;
        }
    }
    
    public User handleLikes(String userName, MovieIdReq req) throws Exception {
        try {
            User currentUser = getUser(userName);          
            List<Long> likes = currentUser.getLikes();
            if(likes.contains(req.getMovieId())) {
                likes.remove(req.getMovieId());
            } else {
                likes.add(req.getMovieId());
            }
            currentUser.setLikes(likes);
            User modifiedUser = createUser(currentUser);
            return modifiedUser;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something Went wrong in Service, while liking!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return null;
        }
    }

    public List<Movie> getMyWatchlist(String userName) {
        User user = this.getUser(userName);
        List<Long> watchlistIds = user.getWatchList();
        List<Movie> myWatchList = new ArrayList<>();

        for(int i = 0; i < watchlistIds.size(); i++) {
            Optional<Movie> currentMovieOp = moviesService.getMovieById(watchlistIds.get(i));
            Movie currentMovie = currentMovieOp.get();
            myWatchList.add(currentMovie);
        }
       
        return myWatchList;

    }


}
