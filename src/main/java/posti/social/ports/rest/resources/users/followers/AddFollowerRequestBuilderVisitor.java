package posti.social.ports.rest.resources.users.followers;

import java.util.UUID;

import posti.social.adapters.FollowUserServiceAdapter;

public class AddFollowerRequestBuilderVisitor implements FollowUserServiceAdapter.RequestBuilderVisitor {
    private UUID followerId;
    private UUID targetId;

    public void setFollowerId(UUID followerId) {
        this.followerId = followerId;
    }

    public void setTargetId(UUID followerId) {
        this.targetId = followerId;
    }

    @Override
    public FollowUserServiceAdapter.RequestBuilder accept(FollowUserServiceAdapter.RequestBuilder builder) {
        return builder.withFollowerId(followerId).withTargetId(targetId);
    }
}
