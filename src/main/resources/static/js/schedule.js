var teamId = null;
var updatedEvent = null;
var isTeamLeader = false;

document.addEventListener('DOMContentLoaded', function () {
    let updateModal = document.getElementById("eventUpdateModal");
    let createModal = document.getElementById("eventCreateModal");
    let addMemberModal = document.getElementById("addMemberModal");
    let createTeamModal = document.getElementById("createTeamModal");
    let updateTeamModal = document.getElementById("updateTeamModal");

    let teamTabs = document.getElementById('teamTabs');

    let addMemberButton = document.getElementById('addMemberModalButton')
    let addMemberSubmitButton = document.getElementById('addMemberSubmitButton');

    let createTeamButton = document.getElementById("createTeamButton");
    let createTeamSubmitButton = document.getElementById('createTeamSubmitButton');

    let updateTeamButton = document.getElementById('updateTeamButton')
    let updateTeamSubmitButton = document.getElementById('updateTeamSubmitButton');

    let removeMemberButton = document.getElementById('removeMemberButton');


    initialize();

    function initialize() {

        teamTabs.addEventListener('click', handleTabClick);

        addMemberButton.addEventListener('click', handleAddMemberButtonClick);
        addMemberSubmitButton.addEventListener('click', handleAddMemberSubmitButtonClick);

        createTeamButton.addEventListener('click', handleCreateTeamButtonClick);
        createTeamSubmitButton.addEventListener('click', handleCreateTeamSubmitButtonClick);

        updateTeamButton.addEventListener('click', handleUpdateTeamButtonClick);
        updateTeamSubmitButton.addEventListener('click', handleUpdateTeamSubmitButtonClick);

        removeMemberButton.addEventListener('click', handleRemoveMemberButtonClick);

        var defaultTab = teamTabs.querySelector('.nav-link');
        if (defaultTab) {
            defaultTab.classList.add('active');
            teamId = defaultTab.getAttribute('data-teamId');
            isTeamLeader = checkTeamLeader(teamId);
            renderTeamTabs();
            updateTeamTab(teamId);
            updateMemberButtonsVisibility();
        }

        window.addEventListener('click', event => {
            closeModalIfClickedOutside(event, updateModal);
            closeModalIfClickedOutside(event, createModal);
            closeModalIfClickedOutside(event, addMemberModal);
            closeModalIfClickedOutside(event, createTeamModal);
            closeModalIfClickedOutside(event, updateTeamModal);
        });
    }

    function fetchJSON(url, method, body = null) {
        const requestOptions = {
            method: method,
            body: body,
        };

        return fetch(url, requestOptions)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Network response was not ok: ${response.status}`);
                }
                return response.json();
            })
            .catch(error => {
                console.error('Fetch error:', error);
            });
    }


    function handleUpdateTeamButtonClick() {
        console.log('updateTeamButton clicked. teamId = ', teamId);
        updateTeamModal.style.display = "block";
    }

    function handleUpdateTeamSubmitButtonClick(event) {
        event.preventDefault(); // 폼 기본 동작 막기
        var teamNameInput = document.getElementById('updateTeamName');
        var name = teamNameInput.value.trim(); // 입력값 앞뒤 공백 제거
        if (name) {
            var form = new FormData();
            form.append("name", name);
            form.append("teamId", teamId);

            fetch('/team/update', {
                method: 'POST',
                body: form
            })
                .then(response => {
                        if (response.ok) {
                            return response.json();
                        } else {
                            throw new Error('팀 수정 요청 실패');
                        }
                    }
                )
                .then(data => {
                    var tabItem = document.querySelector(`.tabItem[data-teamId="${teamId}"]`);
                    if (tabItem) {
                        teamNameInput.value = "";
                        updateTeamModal.style.display = 'none';
                        var activeTabs = teamTabs.querySelectorAll('.nav-link.active');
                        activeTabs.forEach(function (tab) {
                            tab.classList.remove('active');
                        });
                        tabItem.classList.add('active');
                        isTeamLeader = checkTeamLeader(teamId);
                        renderTeamTabs();
                        renderTeamCalendar(teamId);
                    }

                })
                .catch(error => {
                    console.error('팀 수정 에러:', error);
                });
        }
    }

    function handleCreateTeamSubmitButtonClick(event) {
        event.preventDefault(); // 폼 기본 동작 막기

        var teamNameInput = document.getElementById('createTeamName');
        var name = teamNameInput.value.trim(); // 입력값 앞뒤 공백 제거
        if (name) {
            var form = new FormData();
            form.append("name", name);

            fetch('/team/create', {
                method: 'POST',
                body: form
            })
                .then(response => {
                        if (response.ok) {
                            return response.json();
                        } else {
                            throw new Error('팀 생성 요청 실패');
                        }
                    }
                )
                .then(data => {
                    teamId = data.id;

                    teamNameInput.value = "";
                    createTeamModal.style.display = 'none';
                    isTeamLeader = checkTeamLeader(teamId);
                    renderTeamTabs();
                })
                .catch(error => {
                    console.error('팀 생성 에러:', error);
                });
        }
    }

    function handleAddMemberSubmitButtonClick(event) {
        event.preventDefault(); // 폼 기본 동작 막기
        var username = document.getElementById('addMemberUsername').value;
        if (username) {
            var form = new FormData();
            form.append("teamId", teamId);
            form.append("username", username);

            fetch('/team/addMember', {
                method: 'POST',
                body: form
            })
                .then(response => response.text())
                .then(data => {
                    addMemberModal.style.display = 'none';
                    document.getElementById('addMemberUsername').value = "";
                    renderTeamMembers(teamId);
                })
                .catch(error => {
                    console.error('팀 멤버 추가 에러:', error);
                });
        }
    }

    function handleAddMemberButtonClick() {
        addMemberModal.style.display = "block";
    }

    function handleCreateTeamButtonClick() {
        createTeamModal.style.display = "block";
    }

    function handleRemoveMemberButtonClick() {
        let loggedInMemberId = document.getElementById('loggedInMemberId').getAttribute('data-username');
        let teamMembersList = document.getElementById('teamMembersList');
        let memberItems = teamMembersList.querySelectorAll('.team-member-item');

        memberItems.forEach(function (memberItem) {
            let existingButton = memberItem.querySelector('.remove-member-button');

            if (!existingButton) {
                let username = memberItem.getAttribute('data-member-id');
                console.log('username = ', username);
                console.log('loggedInMemberId = ', loggedInMemberId);
                if (username !== loggedInMemberId) {
                    let removeButton = document.createElement('button');
                    removeButton.classList.add('remove-member-button', 'btn', 'btn-danger');
                    removeButton.textContent = '-';
                    removeButton.style.marginLeft = '10px';
                    removeButton.style.borderRadius = '50%';
                    removeButton.style.height = '30px';
                    removeButton.style.width = '30px';
                    removeButton.style.padding = '0';


                    removeButton.addEventListener('click', function () {
                        var form = new FormData();
                        form.append("teamId", teamId);
                        form.append("username", username);

                        fetch('/team/removeMember', {
                            method: 'POST',
                            body: form
                        })
                            .then(response => response.text())
                            .then(data => {
                                renderTeamMembers(teamId);
                            })
                            .catch(error => {
                                console.error('Error removing member:', error);
                            });
                    });

                    memberItem.appendChild(removeButton);
                }
            } else {
                existingButton.parentNode.removeChild(existingButton);
            }
        });
    }

    function updateTeamTab(newTeamId) {
        const tabItem = document.querySelector(`.tabItem[data-teamId="${newTeamId}"]`);
        if (tabItem) {
            const activeTabs = teamTabs.querySelectorAll('.nav-link.active');
            activeTabs.forEach(tab => tab.classList.remove('active'));
            tabItem.classList.add('active');
            teamId = newTeamId;
            isTeamLeader = checkTeamLeader(teamId);
            updateMemberButtonsVisibility();
            renderTeamCalendar(teamId);
        }
    }

    function handleTabClick(event) {
        const tabItem = event.target.closest('.tabItem');
        if (tabItem) {
            const teamId = tabItem.getAttribute('data-teamId');
            updateTeamTab(teamId);
            updateMemberButtonsVisibility();
        } else if (event.target.id === 'updateTeamButton') {
            updateTeamModal.style.display = "block";
        }
    }

    function closeModalIfClickedOutside(event, modal) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }

    function checkTeamLeader(teamId) {
        return fetchJSON(`/team/checkTeamLeader?teamId=${teamId}`)
            .then(isLeader => isLeader === true)
            .catch(() => false);
    }

    // 멤버 추가/제거 버튼 표시 여부 업데이트
    async function updateMemberButtonsVisibility() {
        checkTeamLeader(teamId)
            .then(isLeader => {
                addMemberButton.style.display = isLeader ? 'block' : 'none';
                removeMemberButton.style.display = isLeader ? 'block' : 'none';
            })
            .catch(error => {
                console.error('Error updating member button visibility:', error);
            });
    }

    function renderTeamTabs() {
        // 기존 탭들을 모두 제거
        while (teamTabs.firstChild) {
            teamTabs.removeChild(teamTabs.firstChild);
        }

        // 팀 목록을 가져와서 탭을 생성
        fetchJSON('/team/teams')
            .then(data => {
                data.forEach(function (team) {
                    let tabItem = document.createElement('a');
                    tabItem.textContent = team.name;
                    tabItem.classList.add('nav-item', 'nav-link', 'tabItem');
                    tabItem.setAttribute('data-teamId', team.id);

                    let hiddenInput = document.createElement('input');
                    hiddenInput.type = 'hidden';
                    hiddenInput.id = 'selectedTeamLeader';
                    hiddenInput.value = team.teamLeader;

                    let editButton = document.createElement('button');
                    editButton.textContent = '수정';
                    editButton.id = 'updateTeamButton';
                    editButton.addEventListener('click', function () {
                        updateTeamModal.style.display = "block";
                    });

                    let tabContainer = document.createElement('div');
                    tabContainer.classList.add('tab-container');
                    tabContainer.appendChild(tabItem);
                    tabContainer.appendChild(hiddenInput);
                    tabContainer.appendChild(editButton);

                    teamTabs.appendChild(tabContainer);
                });

                let tabItem = document.querySelector(`.tabItem[data-teamId="${teamId}"]`);
                if (tabItem) {
                    var activeTabs = teamTabs.querySelectorAll('.nav-link.active');
                    activeTabs.forEach(function (tab) {
                        tab.classList.remove('active');
                    });
                    tabItem.classList.add('active');
                    isTeamLeader = checkTeamLeader(teamId);
                    renderTeamCalendar(teamId);
                }

                teamId = tabItem.getAttribute('data-teamId');
                isTeamLeader = checkTeamLeader(teamId);
                updateMemberButtonsVisibility();

                var createTeamButton = document.createElement('button');
                createTeamButton.id = 'createTeamButton';
                createTeamButton.classList.add('nav-item', 'nav-link');
                createTeamButton.textContent = '➕';
                createTeamButton.addEventListener('click', function () {
                    createTeamModal.style.display = "block";
                });

                teamTabs.appendChild(createTeamButton);

                // 팀 탭에 이벤트 리스너 연결
                teamTabs.addEventListener('click', function (event) {
                    if (event.target.classList.contains('tabItem')) {
                        var activeTabs = teamTabs.querySelectorAll('.nav-link.active');
                        activeTabs.forEach(function (tab) {
                            tab.classList.remove('active');
                        });
                        event.target.classList.add('active');

                        teamId = event.target.getAttribute('data-teamId');
                        isTeamLeader = checkTeamLeader(teamId);
                        updateMemberButtonsVisibility();
                    }
                });
            })
            .catch(error => {
                console.error('Error fetching team data:', error);
            });
    }

    function renderTeamCalendar(teamId) {
        let teamCalendarEl = document.getElementById('calendar');
        let teamCalendar = new FullCalendar.Calendar(teamCalendarEl, {
            initialView: 'dayGridMonth',
            events: function (fetchInfo, successCallback, failureCallback) {
                fetchJSON(`/schedule/${teamId}/schedules`)
                    .then(data => {
                        console.log(data);
                        var events = data.map(function (event) {
                            return {
                                id: event.id,
                                title: event.title,
                                start: event.scheduleTime,
                                description: event.description,
                                scheduleMembers: event.scheduleMembers,
                            };
                        });
                        successCallback(events);
                    })
                    .catch(error => {
                        console.error('Error fetching team schedules:', error);
                        failureCallback(error);
                    });
            }
            , dateClick: function (info) {
                document.getElementById('createId').value = "";
                document.getElementById('createTitle').value = "";
                document.getElementById('createScheduleTime').value = moment(info.date).format('YYYY-MM-DDTHH:mm');
                document.getElementById('createDescription').value = "";
                document.getElementById('createScheduleMembers').value = "";

                createModal.style.display = "block";
            },

            eventClick: function (info) {
                let event = info.event;
                document.getElementById('updateId').value = event.id;
                document.getElementById('updateTitle').value = event.title;
                document.getElementById('updateScheduleTime').value = moment(event.start).format('YYYY-MM-DDTHH:mm');
                document.getElementById('updateDescription').value = event.extendedProps.description;
                document.getElementById('updateScheduleMembers').value = event.extendedProps.scheduleMembers;
                updatedEvent = event;

                updateModal.style.display = "block";
            }
        });
        teamCalendar.render();
        renderTeamMembers(teamId);
    }

    function renderTeamMembers(teamId) {
        let teamMembersList = document.getElementById('teamMembersList');

        // 기존 멤버 목록 초기화
        while (teamMembersList.firstChild) {
            teamMembersList.removeChild(teamMembersList.firstChild);
        }

        // 팀 멤버 정보를 가져와서 목록에 추가
        fetchJSON(`/team/${teamId}/members`)
            .then(data => {
                data.forEach(function (member, index) {

                    let memberItem = document.createElement('div');
                    memberItem.className = 'team-member-item';
                    memberItem.style.display = 'flex';
                    memberItem.style.alignItems = 'center';
                    memberItem.style.marginBottom = '10px';
                    memberItem.style.justifyContent = 'space-between';
                    memberItem.setAttribute('data-member-id', member.username);

                    let profile = document.createElement('div');
                    profile.style.display = 'flex';
                    profile.style.alignItems = 'center';

                    let profileImgDiv = document.createElement('div');
                    profileImgDiv.style.maxWidth = '40px';
                    profileImgDiv.style.maxHeight = '40px';
                    profileImgDiv.style.borderRadius = '50%';
                    profileImgDiv.style.overflow = 'hidden';
                    profileImgDiv.style.marginRight = '10px';

                    let profileImg = document.createElement('img');
                    profileImg.style.width = '100%';
                    profileImg.style.height = '100%';
                    profileImg.style.objectFit = 'cover';

                    if (member.profileImg) {
                        profileImg.src = `/images/${member.profileImg.storeFileName}`;
                    } else {
                        profileImg.src = '/images/defaultProfileImg.png';
                    }

                    profileImgDiv.appendChild(profileImg);
                    profile.appendChild(profileImgDiv);

                    let memberInfoDiv = document.createElement('div');
                    memberInfoDiv.style.display = 'flex';
                    memberInfoDiv.style.flexDirection = 'column';


                    let nicknameDiv = document.createElement('div');
                    nicknameDiv.style.fontSize = '16px';
                    nicknameDiv.style.fontWeight = 'bold';
                    nicknameDiv.style.marginBottom = '0';

                    let selectedTeamLeader = document.getElementById('selectedTeamLeader').value;
                    if (selectedTeamLeader === member.username) {
                        nicknameDiv.textContent = `${member.nickname} 👑`;
                    } else {
                        nicknameDiv.textContent = member.nickname;
                    }


                    let usernameDiv = document.createElement('div');
                    usernameDiv.style.fontSize = '13px';
                    usernameDiv.style.marginTop = '0';
                    usernameDiv.textContent = `@${member.username}`;

                    memberInfoDiv.appendChild(nicknameDiv);
                    memberInfoDiv.appendChild(usernameDiv);
                    profile.appendChild(memberInfoDiv);
                    memberItem.appendChild(profile);

                    teamMembersList.appendChild(memberItem);

                    // 팀원 사이에 구분선 추가
                    if (index < data.length - 1) {
                        let divider = document.createElement('hr');
                        divider.style.width = '100%';
                        divider.style.borderTop = '2px solid #bcbcbc';
                        divider.style.margin = '10px 0';
                        teamMembersList.appendChild(divider);
                    }
                });
            })
            .catch(error => {
                console.error('Error fetching team members:', error);
            });
    }

    window.addEventListener('click', function (event) {

        if (event.target === updateModal) {
            var form = new FormData();
            form.append("teamId", teamId);
            form.append("id", updatedEvent.id);
            form.append("title", document.getElementById('updateTitle').value);
            form.append("scheduleTime", document.getElementById('updateScheduleTime').value);
            form.append("description", document.getElementById('updateDescription').value);
            form.append("scheduleMembers", document.getElementById('updateScheduleMembers').value);

            fetchJSON('/schedule/update', 'POST', form)
                .then(data => {
                    renderTeamCalendar(teamId);
                })
                .catch(error => {
                    console.error('Schedule Update Error:', error);
                });

            updatedEvent = null;
            updateModal.style.display = "none";
        }

        if (event.target === createModal) {
            var form = new FormData();
            form.append("teamId", teamId);
            form.append("title", document.getElementById('createTitle').value);
            form.append("scheduleTime", document.getElementById('createScheduleTime').value);
            form.append("description", document.getElementById('createDescription').value);
            form.append("scheduleMembers", document.getElementById('createScheduleMembers').value);

            fetchJSON('/schedule/create', 'POST', form)
                .then(data => {
                    renderTeamCalendar(teamId);

                })
                .catch(error => {
                    console.error('Schedule Update Error:', error);
                });
            createModal.style.display = "none";
        }

        if (event.target == addMemberModal) {
            addMemberModal.style.display = "none";
            document.getElementById('addMemberUsername').value = "";
        }
        if (event.target == createTeamModal) {
            createTeamModal.style.display = "none";
            document.getElementById('createTeamName').value = "";
        }
        if (event.target == updateTeamModal) {
            updateTeamModal.style.display = "none";
            document.getElementById('updateTeamName').value = "";
        }
    });
});