//
//  BMSQ_ModifySignController.m
//  BMSQC
//
//  Created by djx on 15/8/2.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ModifySignController.h"

@interface BMSQ_ModifySignController ()

@end

@implementation BMSQ_ModifySignController

@synthesize modifyInfo;
@synthesize mobileNbr;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setNavigationBar];
    [self setNavTitle:@"个性签名"];
    [self setNavBackItem];
    
    self.textView  = [[UITextView alloc]initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y + 10, APP_VIEW_WIDTH - 20, 200)];
    self.textView.delegate = self;
    [self.view addSubview:self.textView];
    self.textView.text = modifyInfo;
    self.uilabel = [[UILabel alloc]initWithFrame:CGRectMake(5, 5, APP_VIEW_WIDTH - 30, 25)];
    
    if (modifyInfo == nil || modifyInfo.length <= 0)
    {
        self.uilabel.text = @"这个家伙很懒,什么也没有留下.";
    }
    
    [self.uilabel setTextColor:[UIColor colorWithRed:102.0/255.0 green:102.0/255.0 blue:102.0/255.0 alpha:1.0]];
    self.uilabel.enabled = NO;//lable必须设置为不可用
    self.uilabel.backgroundColor = [UIColor clearColor];
    [self.textView addSubview:self.uilabel];
    self.textView.delegate = self;
    self.uilabel.font = self.textView.font;
    
    UIButton* btnRightBar = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44)];
    [btnRightBar addTarget:self action:@selector(sendButtonClick) forControlEvents:UIControlEventTouchUpInside];
    [btnRightBar setTitle:@"提交" forState:UIControlStateNormal];
    [self setNavRightBarItem:btnRightBar];

}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:YES];
    [self.view bringSubviewToFront:_uilabel];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)textViewDidChange:(UITextView *)_textview
{
    
    if (_textView.text.length == 0)
    {
        self.uilabel.text = @"这个家伙很懒,什么也没有留下.";
    }else{
        self.uilabel.text = @"";
    }
}

-(void)sendButtonClick
{
    
    if (_textView.text.length==0) {
        
        UIAlertView* alert=[[UIAlertView alloc]initWithTitle:nil message:@"请输入个性签名" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    
    [self updateUserInfo];
}

- (void)updateUserInfo
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:mobileNbr forKey:@"mobileNbr"];
    [params setObject:self.textView.text forKey:@"nickName"];

    NSString* vcode = [gloabFunction getSign:@"updateUserInfo" strParams:mobileNbr];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"updateUserInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if ([[responseObject objectForKey:@"code"] integerValue] == 50000)
        {
            [self showAlertView:@"修改成功"];
        }
        else
        {
            [self showAlertView:@"修改失败"];
        }
        [ProgressManage closeProgress];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    [self.navigationController popViewControllerAnimated:YES];
    
}


#pragma UITextView Delegate Method

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    
    if([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        return NO;
    }
    
    return YES;
}

- (void)viewDidUnload {
    [self setTextView:nil];
    [super viewDidUnload];
}
- (IBAction)didClickSendButton:(id)sender {
    
    [self sendButtonClick];
}


@end
