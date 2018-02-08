import "./videoChatPage.scss";
import template from "./videoChatPage.pug";

// import OT from '@opentok/client'
const OT = require('@opentok/client');

const apiKey = 46010202;
const sessionId = "2_MX40NjAxMDIwMn5-MTUxMTk4NTQ1OTA2OH5uc1R0aXdjVGdrWGJaWWVySUN4VHFZaUN-fg";
const token = "T1==cGFydG5lcl9pZD00NjAxMDIwMiZzaWc9NDIxYTg4NjUxNmYxYzNkODY0OTYxNWNjMDA0NDE2NzFlYTUyZjRhYzpzZXNzaW9uX2lkPTJfTVg0ME5qQXhNREl3TW41LU1UVXhNVGs0TlRRMU9UQTJPSDV1YzFSMGFYZGpWR2RyV0dKYVdXVnlTVU40VkhGWmFVTi1mZyZjcmVhdGVfdGltZT0xNTEyMDI2NzU4Jm5vbmNlPTAuNzIxMTMwMDg5NjMwMDg0MSZyb2xlPW1vZGVyYXRvciZleHBpcmVfdGltZT0xNTEyMDQ4MzU3JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";

// Handling all of our errors here by alerting them
function handleError(error) {
	if (error) {
		alert(error.message);
	}
}

export default {
	controller: function ($scope, $stateParams, $state, authenticationService) {
		"ngInject";

		console.log($stateParams);

		const isTutor = $state.current.data.isTutor;
		const sessionId = $stateParams.sessionId;

		$scope.session = null;

		getSessionInfo().then((resp) => {

				if (resp.status >= 400) {
					alert(resp.data.message)
				}
				initializeSession(resp.data.apiKey, resp.data.sessionId, resp.data.token);

			},
			(resp) => {
				if (resp.status >= 400) {
					alert(resp.data.message)
					$state.go("app.account.profile.list");
				}
			}
		);

		function getSessionInfo() {
			if (isTutor)
				return authenticationService.joinTutorSession(sessionId)
			else
				return authenticationService.joinStudentSession(sessionId)
		}

		function initializeSession(apiKey, sessionId, token) {
			let session = OT.initSession(apiKey, sessionId);
			$scope.session = session;

			// Subscribe to a newly created stream
			session.on('streamCreated', function (event) {
				session.subscribe(event.stream, 'subscriber', {
					insertMode: 'append',
					width: '100%',
					height: '100%'
				}, handleError);
			});

			// Create a publisher
			let publisher = OT.initPublisher('publisher', {
				insertMode: 'append',
				width: '100%',
				height: '100%'
			}, handleError);

			// Connect to the session
			session.connect(token, function (error) {
				// If the connection is successful, initialize a publisher and publish to the session
				if (error) {
					handleError(error);
				} else {
					session.publish(publisher, handleError);
				}
			});
		}

		$scope.endSession = function () {
			console.log("disconnected!");
			$scope.session.disconnect();

			// TODO: popup review

			// TODO: mark session ended
			authenticationService.endSession(sessionId);

			$state.go("app.account.profile.list")
		};

		// in case they navigate away. Kill the session.
		$scope.$on('$destroy', function () {
			if ($scope.session) {
				$scope.session.disconnect();
				$scope.session = undefined;
			}
		});
	},
	template: template
}
