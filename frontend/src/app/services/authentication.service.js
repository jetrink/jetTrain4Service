"use strict";

export default {
	init: (app) => {
		app.factory('authenticationService', function authenticationService($http, $rootScope, $window,$state) {
			"ngInject";
			const userTokenKey = "_userToken";
			let vm = {
				isSetup: null,
				isStudent: null,
				isTutor: null,
				isManager: null,
				isApproved: false
			};

			return {
				isAuthorized: isAuthorized,
				authenticateUser: authenticate,
				setUserToken: setUserToken,
				getUserToken: getUserToken,
				clearAuthInfo: clearAuthInfo,
				logout: logout,
				getUserInfo: getUserInfo,
				signUp: signUp,
				checkUsername: checkUsername,
				studentProfileSetup: studentProfileSetup,
				tutorProfileSetup:tutorProfileSetup,
				managerProfileSetup:managerProfileSetup,
				getSelf: getSelf,
				setUserInfo: setUserInfo,
				getAllSubjects: getAllSubjects,
				getSubjects:getSubjects,
				getTutorRequests:getTutorRequests,
				getStudentRequests:getStudentRequests,
				respondToRequest:respondToRequest,
				findByTutor:findByTutor,
				findBySubject:findBySubject,
				findByTutorName:findByName,
				getTutorSubjects:getTutorSubjects,
				registerSession:register,
				isApproved: isApproved,
				setApproved: setApproved,
				getStudentHistory:getStudentHistory,
				getTutorHistory: getTutorHistory,
				getStudentProfile: getStudentProfile,
				getManagerProfile:getManagerProfile,
				getTutorProfile:getTutorProfile,
				getATutorProfile:getATutor,
				getTutorSchedule:getTutorSchedule,
				updateStudent:updateStudent,
				updateManager:updateManager,
				updateTutor:updateTutor,
				getManagerDashboard:getManagerDashboard,
				getTutorDashboard:getTutorDashboard,
				getStudentDashboard:getStudentDashboard,
				getManagerSessionHistory:getManagerSessionHistory,
				getManagerRequest:getManagerRequest,
				setUpStudent: setUpStudent,
				getProfile:getProfile,
				setAvailability:setAvailability,

				loginAdmin: loginAdmin,
				logoutAdmin: logoutAdmin,
				getAdminTutors:getAdminTutors,
				adminApprove:adminApprove,
				adminDeny:adminDeny,
				getAdmins:getAdmins,
				deleteAdmin:deleteAdmin,
				createAdmin:createAdmin,
				getAdminSubjects:getAdminSubjects,
				createSubject:createSubject,
				editSubject:editSubject,
				deleteSubject:deleteSubject,

				joinStudentSession: joinStudentSession,
				joinTutorSession: joinTutorSession,
				endSession: endSession

			};
			function deleteSubject(id) {
				return http("DELETE","./api/admin/subjects/"+id);
			}
			function editSubject(data,id) {
				return http("PUT","./api/admin/subjects/"+id,data);
			}
			function createSubject(data) {
				return http("POST","./api/admin/subjects",data);
			}
			function getAdminSubjects() {
				return http("GET","./api/admin/subjects");
			}
			function createAdmin(data) {
				return http("POST","./api/admin/accounts",data);
			}
			function deleteAdmin(id) {
				return http("DELETE","./api/admin/accounts/"+id);
			}
			function getAdmins() {
				return http("GET","./api/admin/accounts");
			}

			function adminApprove(id) {
				return http("GET","./api/admin/tutors/"+id+"/approve");
			}
			function adminDeny(id) {
				return http("DELETE","./api/admin/tutors/"+id, undefined);
			}

			function getAdminTutors() {
				return $http({
					url: "./api/admin/tutors",
					method: "GET",
					params: {approved: false},
					headers: {
						'x-csrf': getUserToken().CSRF_TOKEN
					}
				});
			}

			function logoutAdmin() {
				http('GET','./api/admin/auth/logout').then(function () {
					clearAuthInfo();
					$state.go('app.admin.login');
				}, function (err) {
					console.log(err.data);
				});
			}

			function loginAdmin(data) {
				return $http.post("./api/admin/auth/login",data);
			}

			function setAvailability(data) {
				return http("POST","./api/dashboard/isavailable",data);
			}

			function getProfile() {
				return http("GET","./api/profiles/user");
			}

			function setUpStudent(data) {
				return http("POST","./api/dashboard/addstudent", data);
			}

			function getManagerRequest(id) {
				return http("GET","./api/sessions/students/requests/"+id);
			}

			function getManagerSessionHistory(id) {
				return http("GET","./api/sessions/students/"+id);
			}

			function joinStudentSession(sessionId) {
				return http("GET", `./api/opentok/${sessionId}/student`);
			}

			function joinTutorSession(sessionId) {
				return http("GET", `./api/opentok/${sessionId}/tutor`);
			}

			function endSession(sessionId) {
				return http("DELETE", `./api/opentok/${sessionId}`);
			}

			function getManagerDashboard() {
				return http("GET","./api/dashboard/Parent");
			}

			function getStudentDashboard() {
				return http("GET","./api/dashboard/student");
			}

			function getTutorDashboard() {
				return http("GET","./api/dashboard/Tutor");
			}

			function updateManager(data) {
				return http("PUT","./api/profiles/parent",data);
			}

			function updateTutor(data) {
				return http("PUT","./api/profiles/tutor",data);
			}

			function updateStudent(data) {
				return http("PUT","./api/profiles/student",data)
			}
			function getStudentProfile() {
				return http("GET","./api/profiles/student");
			}

            function getManagerProfile() {
                return http("GET","./api/profiles/parent");
            }

			function getTutorProfile() {
				return http("GET","./api/profiles/tutor");
			}

			function getATutor(username) {
				return http("GET","./api/profiles/tutor/"+username);
			}

			function getTutorSchedule(username) {
				return http("GET","./api/profiles/schedule/"+username);
			}

			function getTutorHistory() {
				return http("GET","./api/sessions/self/tutorhistory");
			}

			function getStudentHistory() {
				return http("GET","./api/sessions/self/studenthistory");
			}

			function setApproved(data) {
				vm.isApproved = data.approved;
			}
			function isApproved() {
				return http("GET","./api/profiles/isapproved");
			}

			function findByName(data) {
				return http("POST","./api/sessions/findtutor/bytutorname",data);
			}

			function register(data) {
				return http("POST","./api/sessions/register",data);
			}

			function getTutorSubjects(data) {
				return http("GET","./api/sessions/tutor/subjects/"+data);
			}

			function findBySubject(data) {
				return http("POST","./api/sessions/findtutor/bysubject",data);
			}

			function findByTutor(data) {
				return http("POST","./api/sessions/findtutor/bytutor",data);
			}

			function respondToRequest(data) {
				return http("POST","./api/sessions/requests/respond",data);
			}

			function getTutorRequests() {
				return http("GET","./api/sessions/tutors/requests/self");
			}

			function getStudentRequests() {
				return http("GET","./api/sessions/students/requests/self");
			}

			function getSubjects(field) {
				return http("GET","./api/profiles/subjects/"+field);
			}

			function getAllSubjects() {
				return http("GET",'./api/profiles/subjects');
			}

			function setUserInfo(info) {
				if (info.studentId !== null) {
					vm.isStudent = true;
				} else {
					vm.isStudent = false;
				}
				if (info.tutorId !== null) {
					vm.isTutor = true;
				} else {
					vm.isTutor = false;
				}
				vm.isManager = info.studentManager;

				if (!vm.isStudent && !vm.isTutor && !vm.isManager) {
					vm.isSetup = true;
				} else {
					vm.isSetup = false;
				}
			}

			function studentProfileSetup(data) {
				return http('POST','./api/profiles/studentsetup',data);
			}

			function tutorProfileSetup(data) {
				return http('POST','./api/profiles/tutorsetup',data);
			}

			function managerProfileSetup(data) {
				return http('POST','./api/profiles/managersetup',data);
			}

			function checkUsername(userName) {
				return $http({
					url: './api/users/checkUsername',
					method: "GET",
					params: {username: userName}
				});
			}

			function logout() {
				http('GET','./api/users/logout').then(function () {
					clearAuthInfo();
					$state.go('app.auth');
				}, function (err) {
					console.log(err.data);
				});
			}

			function authenticate(userData) {
				return $http.post('./api/users/login', userData);
			}

			function getSelf() {
				return http('GET','./api/users/self');
			}


			function getUserInfo() {
				return vm;
			}

			function isAuthorized(userType) {
				if (userType.includes('profileSetup')) {
					return vm.isSetup;
				}
				else if (userType.includes('student')) {
					return vm.isStudent;
				}
				else if (userType.includes('tutor')) {
					return vm.isTutor;
				}
				else if (userType.includes('manager')) {
					return vm.isManager;
				}
				else if (userType.includes('profile')) {
					return !vm.isSetup;
				}
				else {
					return false;
				}
			}

			function setUserToken(token,bool) {
				clearAuthInfo();
				var userData = {
					systemUser: bool,
					CSRF_TOKEN: token.headers('x-csrf')
				}

				localStorage.setItem(userTokenKey, angular.toJson(userData));
				//sessionStorage.setItem(userTokenKey, angular.toJson(token));
			};

			function clearAuthInfo() {
				clearTokenInfo();
			};

			function clearTokenInfo() {
				localStorage.removeItem(userTokenKey);
				//sessionStorage.removeItem(userTokenKey);
			};

			function getUserToken() {
				let userToken = localStorage.getItem(userTokenKey);
				if (userToken) {
					return angular.fromJson(localStorage.getItem(userTokenKey));
				} else {
					return null;
				}
			};

			function signUp(userData) {
				return $http.post('./api/users/signup', userData);
			}

			function http(method, url, data){
				let req = {
					method: method,
					url: url,
					headers: {
						'x-csrf': getUserToken().CSRF_TOKEN
					}
				};

				if (method !== 'GET' && data) {
					req.data = data;
				}

				return $http(req);
			}

		})
	}
}
