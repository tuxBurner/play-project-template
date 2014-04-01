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

@NodeEntity
@TypeAlias(value = TypeAliasNames.USER)
public class NeoUser extends AbstractNeoNode implements Identity {

  @Indexed(unique = true)
  public String email;

  public String identUserId;

  public String identProviderId;

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
   * @param identity
   * @return
   */
  public static NeoUser save(Identity identity) {


    NeoUser user = findNeoUserByIdentityId(identity.identityId());
    if (user == null) {
      Logger.debug("User is a new user.");
      user = new NeoUser();
    }

    user.authMethod = identity.authMethod().method();

    user.firstName = identity.firstName();
    user.lastName = identity.lastName();
    user.fullName = identity.fullName();
    user.identProviderId = identity.identityId().providerId();
    user.identUserId = identity.identityId().userId();

    if (identity.email().isDefined() == true) {
      user.email = identity.email().get();
    }

    if (identity.avatarUrl().isDefined() == true) {
      user.avatarUrl = identity.avatarUrl().get();
    }

    /**
     * Password login user
     */
    if (identity.passwordInfo().isDefined() && user.id == null) {
      final PasswordInfo passwordInfo = identity.passwordInfo().get();
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
  public static Option<Identity> findByEmailAndProvider(final String email, final String providerId) {
    final Identity byEmailAndProvider = Neo4JServiceProvider.get().neoUserRepository.findByEmailAndProvider(email, providerId);
    return Option.apply(byEmailAndProvider);
  }

  /**
   * Finds the user by the given id
   *
   * @param id
   * @return
   */
  public static Option<Identity> findByIdentityId(IdentityId id) {
    final Identity byIdentity = findNeoUserByIdentityId(id);
    return Option.apply(byIdentity);
  }

  /**
   * Finds the user by the identity
   *
   * @param id
   * @return
   */
  public static NeoUser findNeoUserByIdentityId(IdentityId id) {
    return Neo4JServiceProvider.get().neoUserRepository.findByIdentity(id.userId(), id.providerId());
  }

  /* SECURE SOCIAL STUFF */
  @Override
  public IdentityId identityId() {
    return new IdentityId(identUserId, identProviderId);
  }

  @Override
  public String firstName() {
    return firstName;
  }

  @Override
  public String lastName() {
    return lastName;
  }

  @Override
  public String fullName() {
    return fullName;
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
    /* EO SECURE SOCIAL STUFF */
}
