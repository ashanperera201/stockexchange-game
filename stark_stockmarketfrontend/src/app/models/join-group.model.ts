export class GroupInfo {
    groupName: string;
    joinerName: string;
    joinerId: string;
    creatorId: string;
    players: number;
    creatorName: string;
    profileUrl: string;
    isActive: boolean;
}

export class GroupRequestPayload {
    groupName: string;
    userId: string;
    roundNo: number;
    amount: number;
    photoURL: string;
}