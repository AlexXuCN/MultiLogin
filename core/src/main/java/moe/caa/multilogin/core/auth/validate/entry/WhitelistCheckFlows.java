package moe.caa.multilogin.core.auth.validate.entry;

import lombok.SneakyThrows;
import moe.caa.multilogin.core.auth.validate.ValidateContext;
import moe.caa.multilogin.core.main.MultiCore;
import moe.caa.multilogin.flows.workflows.BaseFlows;
import moe.caa.multilogin.flows.workflows.Signal;

public class WhitelistCheckFlows extends BaseFlows<ValidateContext> {
    private final MultiCore core;

    public WhitelistCheckFlows(MultiCore core) {
        this.core = core;
    }

    @SneakyThrows
    @Override
    public Signal run(ValidateContext validateContext) {
        // 如果没有开启白名单验证
        if (!validateContext.getYggdrasilAuthenticationResult().getYggdrasilServiceConfig().isWhitelist()) {
            return Signal.PASSED;
        }
        // 如果有白名单
        if (core.getSqlManager().getUserDataTable().hasWhitelist(validateContext.getYggdrasilAuthenticationResult().getResponse().getId(),
                validateContext.getYggdrasilAuthenticationResult().getYggdrasilId())) {
            return Signal.PASSED;
        }
        // 踹了
        validateContext.setDisallowMessage(core.getLanguageHandler().getMessage("auth_validate_failed_no_whitelist"));
        return Signal.TERMINATED;
    }
}