# Module simbot-component-kook-stdlib

基于api模块对 KOOK Bot 的最**基础**实现。
stdlib意为标准库，其宗旨在于实现完整的 KOOK Bot 对事件的接收与处理，并尽可能保留事件最原始的状态（不做过多的封装）。

标准库实现 KOOK Bot 最基础的事件监听（`websocket based`），而对于功能交互则需要开发者自行借助 [api模块](simbot-component-kook-api) 中提供的内容来完成，
这可以使得开发者对整个事件处理流程中拥有更多的掌控性或发挥空间。

如果你希望通过更原生的方式开发 KOOK Bot ，可以考虑直接使用stdlib模块。
