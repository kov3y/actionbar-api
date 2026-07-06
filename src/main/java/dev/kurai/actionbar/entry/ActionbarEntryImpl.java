package dev.kurai.actionbar.entry;

import static java.time.Instant.now;
import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/** Default immutable {@link ActionbarEntry} implementation. */
@Getter
@ApiStatus.Internal
final class ActionbarEntryImpl implements ActionbarEntry {

  private final Key key;

  @Getter(AccessLevel.NONE)
  private final ComponentLike componentLike;

  private final Duration duration;

  private final Instant creationTime;

  @Getter(AccessLevel.NONE)
  private final @Nullable Instant expirationTime;

  ActionbarEntryImpl(final Key key, final ComponentLike componentLike, final Duration duration) {
    this.key = requireNonNull(key, "Key cannot be null");

    this.componentLike = requireNonNull(componentLike, "Component cannot be null");

    this.duration = requireNonNull(duration, "Duration cannot be null");
    if (this.duration.isNegative()) {
      throw new IllegalArgumentException("Duration cannot be negative");
    }

    this.creationTime = now();
    this.expirationTime = this.duration.isZero() ? null : this.creationTime.plus(this.duration);
  }

  /** {@inheritDoc} */
  @Override
  public boolean expired() {
    return this.expirationTime != null && !now().isBefore(this.expirationTime);
  }

  /** {@inheritDoc} */
  @Override
  public Component asComponent() {
    return this.componentLike.asComponent();
  }
}
