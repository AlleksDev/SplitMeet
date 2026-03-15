package com.coditos.splitmeet.features.group.domain.usecases

data class GroupUseCases(
    val createGroup: CreateGroupUseCase,
    val deleteGroup: DeleteGroupUseCase,
    val getGroupDetail: GetGroupDetailUseCase,
    val getGroupMembers: GetGroupMembersUseCase,
    val getMyGroups: GetMyGroupsUseCase,
    val inviteMember: InviteMemberUseCase,
    val removeMember: RemoveMemberUseCase,
    val updateGroup: UpdateGroupUseCase
)