package neo4j.models.users;

import neo4j.Neo4JServiceProvider;
import neo4j.TypeAliasNames;
import neo4j.models.AbstractNeoNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import play.Logger;
import scala.Option;
import securesocial.core.*;
import securesocial.core.services.SaveMode;

@NodeEntity
@TypeAlias(value = TypeAliasNames.USER)
public class NeoUser extends AbstractNeoNode implements GenericProfile {

    @Indexed(unique = true)
    public String email;

    public String userId;

    public String providerId;

    public String firstName;

    public String lastName;

    public String fullName;

    public String avatarUrl;

    public String authMethod;

    public boolean admin = false;


    /**
     * PASSWORD STUFF
     */
    public String password;
    public String passwordHasher;
    public String passwordSalt;


    /**
     * Saves the given user
     *
     * @param basicProfile
     * @return
     */
    public static NeoUser save(BasicProfile basicProfile, SaveMode mode) {


        NeoUser user = findNeoUserByIdentityId(basicProfile);
        if (user == null) {
            Logger.debug("User is a new user.");
            user = new NeoUser();
        }

        user.authMethod = basicProfile.authMethod().method();

        user.firstName = basicProfile.firstName().get();
        user.lastName = basicProfile.lastName().get();
        user.fullName = basicProfile.fullName().get();
        user.providerId = basicProfile.providerId();
        user.userId = basicProfile.userId();

        if (basicProfile.email().isDefined() == true) {
            user.email = basicProfile.email().get();
        }

        if (basicProfile.avatarUrl().isDefined() == true) {
            user.avatarUrl = basicProfile.avatarUrl().get();
        }

        // set the password at the user when defined
        if (basicProfile.passwordInfo().isDefined() && (SaveMode.PasswordChange().equals(mode)  || SaveMode.SignUp().equals(mode))) {
            final PasswordInfo passwordInfo = basicProfile.passwordInfo().get();
            user.password = passwordInfo.password();
            user.passwordHasher = passwordInfo.hasher();
            if (passwordInfo.salt().isDefined() == true) {
                user.passwordSalt = passwordInfo.salt().get();
            }
        }

        final NeoUser savedUser = Neo4JServiceProvider.get().neoUserRepository.save(user);

        return savedUser;
    }

    public static NeoUser setUserIsAdmin(final NeoUser user, final boolean isAdmin) {
        user.admin = isAdmin;
        return Neo4JServiceProvider.get().neoUserRepository.save(user);
    }

    /**
     * Finds a NeoUser by the email and the id
     *
     * @param email
     * @param providerId
     * @return
     */
    public static Option<UserProfile> findByEmailAndProvider(final String email, final String providerId) {
        final UserProfile byEmailAndProvider = Neo4JServiceProvider.get().neoUserRepository.findByEmailAndProvider(email, providerId);
        return Option.apply(byEmailAndProvider);
    }

    /**
     * Finds the user by the given id
     *
     * @param userProfile
     * @return
     */
    public static Option<UserProfile> findByIdentityId(UserProfile userProfile) {
        final UserProfile byIdentity = findNeoUserByIdentityId(userProfile);
        return Option.apply(byIdentity);
    }

    /**
     * Finds the user by the identity
     *
     * @param userProfile
     * @return
     */
    public static NeoUser findNeoUserByIdentityId(UserProfile userProfile) {
        return findUserByIdAndProviderId(userProfile.userId(), userProfile.providerId());
    }

    public static NeoUser findUserByIdAndProviderId(final String userId, final String providerId) {
        return Neo4JServiceProvider.get().neoUserRepository.findByIdentity(userId, providerId);
    }


    @Override
    public Option<String> firstName() {
        return Option.apply(firstName);
    }

    @Override
    public Option<String> lastName() {
        return Option.apply(lastName);
    }

    @Override
    public Option<String> fullName() {
        return Option.apply(fullName);
    }

    @Override
    public Option<String> email() {
        return Option.apply(email);
    }

    @Override
    public Option<String> avatarUrl() {
        return Option.apply(avatarUrl);
    }

    @Override
    public AuthenticationMethod authMethod() {
        return new AuthenticationMethod(authMethod);
    }

    @Override
    public Option<OAuth1Info> oAuth1Info() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Option<OAuth2Info> oAuth2Info() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Option<PasswordInfo> passwordInfo() {
        if (StringUtils.isEmpty(password) == false && StringUtils.isEmpty(passwordHasher) == false) {
            final PasswordInfo passwordInfo = new PasswordInfo(passwordHasher, password, Option.apply(passwordSalt));
            return Option.apply(passwordInfo);
        }

        return Option.empty();
    }

    @Override
    public String providerId() {
        return providerId   ;
    }

    @Override
    public String userId() {
        return userId;
    }
    /* EO SECURE SOCIAL STUFF */
}
