package com.coditos.splitmeet.features.group.di

import com.coditos.splitmeet.features.group.domain.repositories.GroupRepository
import com.coditos.splitmeet.features.group.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GroupUseCaseModule {

    @Provides
    @Singleton
    fun provideCreateGroupUseCase(repository: GroupRepository): CreateGroupUseCase {
        return CreateGroupUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteGroupUseCase(repository: GroupRepository): DeleteGroupUseCase {
        return DeleteGroupUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetGroupDetailUseCase(repository: GroupRepository): GetGroupDetailUseCase {
        return GetGroupDetailUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetGroupMembersUseCase(repository: GroupRepository): GetGroupMembersUseCase {
        return GetGroupMembersUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMyGroupsUseCase(repository: GroupRepository): GetMyGroupsUseCase {
        return GetMyGroupsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideInviteMemberUseCase(repository: GroupRepository): InviteMemberUseCase {
        return InviteMemberUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRemoveMemberUseCase(repository: GroupRepository): RemoveMemberUseCase {
        return RemoveMemberUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateGroupUseCase(repository: GroupRepository): UpdateGroupUseCase {
        return UpdateGroupUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGroupUseCases(
        createGroup: CreateGroupUseCase,
        deleteGroup: DeleteGroupUseCase,
        getGroupDetail: GetGroupDetailUseCase,
        getGroupMembers: GetGroupMembersUseCase,
        getMyGroups: GetMyGroupsUseCase,
        inviteMember: InviteMemberUseCase,
        removeMember: RemoveMemberUseCase,
        updateGroup: UpdateGroupUseCase
    ): GroupUseCases {
        return GroupUseCases(
            createGroup = createGroup,
            deleteGroup = deleteGroup,
            getGroupDetail = getGroupDetail,
            getGroupMembers = getGroupMembers,
            getMyGroups = getMyGroups,
            inviteMember = inviteMember,
            removeMember = removeMember,
            updateGroup = updateGroup
        )
    }
}