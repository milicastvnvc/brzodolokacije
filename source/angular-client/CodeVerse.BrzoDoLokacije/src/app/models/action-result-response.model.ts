export class ActionResultResponse {
  public success!: boolean;
  public actionData!: any;
  public errors: Array<string> = [];
  public hasErrors!: boolean;
}
