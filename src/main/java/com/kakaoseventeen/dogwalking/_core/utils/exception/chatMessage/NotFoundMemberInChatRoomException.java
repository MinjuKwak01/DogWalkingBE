package com.kakaoseventeen.dogwalking._core.utils.exception.chatMessage;

import com.kakaoseventeen.dogwalking._core.utils.ChatMessageCode;

public class NotFoundMemberInChatRoomException extends IllegalArgumentException{

    private ChatMessageCode chatMessageCode;

    public NotFoundMemberInChatRoomException(ChatMessageCode chatMessageCode){
        this.chatMessageCode=chatMessageCode;
    }

}
