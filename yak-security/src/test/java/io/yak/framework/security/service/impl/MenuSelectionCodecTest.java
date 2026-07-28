package io.yak.framework.security.service.impl;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class MenuSelectionCodecTest {

  @Test
  void separatesPermissionAndMenuSelections() {
    long encodedMenuId = MenuSelectionCodec.encodeMenuId(12L);

    assertThat(encodedMenuId).isEqualTo(-13L);
    assertThat(MenuSelectionCodec.extractPermissionIds(
            Arrays.asList(3L, encodedMenuId, -1L, 3L, null)))
            .containsExactly(3L);
    assertThat(MenuSelectionCodec.extractMenuIds(
            Arrays.asList(3L, encodedMenuId, encodedMenuId, -1L)))
            .containsExactly(12L);
  }

  @Test
  void rejectsInvalidMenuIdentifiers() {
    assertThatIllegalArgumentException()
            .isThrownBy(() -> MenuSelectionCodec.encodeMenuId(0L));
    assertThatIllegalArgumentException()
            .isThrownBy(() -> MenuSelectionCodec.decodeMenuId(-1L));
    assertThat(MenuSelectionCodec.extractMenuIds(
            Collections.emptyList())).isEmpty();
  }
}
