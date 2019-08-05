import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable()
export class UserService {

  userProfile: any

  /**
   * Creates an instance of user service.
   * @param http 
   */
  constructor(private http: HttpClient) {
    this.userProfile = JSON.parse(sessionStorage.getItem('user-profile'));
  }

  /**
   * Get user id of user service
   */
  getUserId = () => {
    if (this.userProfile)
      return this.userProfile[0].verifiedUser.userId;
    return null;
  }

  /**
   * Update local user of user service
   */
  updateLocalUser = (updatedUser: any): void => {
    let user: any[] = JSON.parse(sessionStorage.getItem("user-profile"));
    sessionStorage.removeItem("user-profile");
    user[0].verifiedUser = {}
    user[0].verifiedUser = updatedUser;
    sessionStorage.setItem("user-profile", JSON.stringify(user))
  }

  /**
   * Get user name of user service
   */
  getUserName = () => {
    if (this.userProfile)
      return this.userProfile[0].verifiedUser.userName;
    return null;
  }

  /**
   * Get user profile picture of user service
   */
  getUserProfilePicture = () => {
    if (this.userProfile)
      return this.userProfile[0].photoURL;
    return null;
  }

  /**
   * Get verified user of user service
   */
  getVerifiedUser = (): any => {
    let verifiedUser: any;
    this.userProfile.forEach(userResult => {
      verifiedUser = userResult.verifiedUser;
    });
    return verifiedUser;
  }

  /**
   * Get profile of user service
   */
  getProfile = () => {
    this.userProfile = [];
    this.userProfile = JSON.parse(sessionStorage.getItem('user-profile'));
    if (this.userProfile)
      return this.userProfile;
    return null;
  }

  /**
   * Get user of user service
   */
  getUser = (userId: number): Observable<any> => {
    let url: string = environment.apiUrl + 'action?userId=' + userId;
    return this.http.get(url);
  }

  /**
   * Edit user of user service
   */
  editUser = (user: any): Observable<any> => {
    let url: string = environment.apiUrl + 'userUpdate';
    return this.http.post(url, user);
  }
}






