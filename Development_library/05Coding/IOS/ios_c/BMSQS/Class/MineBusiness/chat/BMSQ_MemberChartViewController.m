//
//  LEKnowChartVC.m
//  气泡
//
//  Created by zzy on 14-5-13.
//  Copyright (c) 2014年 zzy. All rights reserved.
//

#import "BMSQ_MemberChartViewController.h"
#import "ChartMessage.h"
#import "ChartCellFrame.h"
#import "ChartCell.h"
#import "KeyBordVIew.h"
#import <AudioToolbox/AudioToolbox.h>
#import <AVFoundation/AVFoundation.h>
#import "UIImage+StrethImage.h"
#import "NSString+DocumentPath.h"
#import <QuartzCore/QuartzCore.h>
#import "MobClick.h"
@interface BMSQ_MemberChartViewController ()<UITableViewDataSource,UITableViewDelegate,KeyBordVIewDelegate,ChartCellDelegate,AVAudioPlayerDelegate>
{
    
    NSString *userImgurl;
    
}
@property (nonatomic,strong) UITableView *tableView;
@property (nonatomic,strong) KeyBordVIew *keyBordView;
@property (nonatomic,strong) NSMutableArray *cellFrames;
@property (nonatomic,assign) BOOL recording;
@property (nonatomic,strong) NSString *fileName;
@property (nonatomic,strong) AVAudioRecorder *recorder;
@property (nonatomic,strong) AVAudioPlayer *player;
@end
static NSString *const cellIdentifier=@"QQChart";
@implementation BMSQ_MemberChartViewController

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"MemberChart"];
}


-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self getUserInfo];
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardShow:) name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardHide:) name:UIKeyboardWillHideNotification object:nil];
    [MobClick beginLogPageView:@"MemberChart"];
}

-(void)keyboardShow:(NSNotification *)note
{
    CGRect keyBoardRect=[note.userInfo[UIKeyboardFrameEndUserInfoKey] CGRectValue];
    CGFloat deltaY=keyBoardRect.size.height;
        [UIView animateWithDuration:[note.userInfo[UIKeyboardAnimationDurationUserInfoKey] floatValue] animations:^{
            
            CGRect rect = self.tableViewRect;
            CGRect rect1 = self.keyboardRect;
            
            rect.size.height = rect.size.height-rect1.size.height-deltaY+64;
            self.tableView.frame = rect;
            
            rect1.origin.y = rect1.origin.y-deltaY;
            self.keyBordView.frame =rect1;
            
            [self scrollViewToBottom:YES];
            
        }];
}

-(void)keyboardHide:(NSNotification *)note
{
    [UIView animateWithDuration:[note.userInfo[UIKeyboardAnimationDurationUserInfoKey] floatValue] animations:^{
        
        self.tableView.frame = self.tableViewRect;
        self.keyBordView.frame =self.keyboardRect;
        
    }];
    
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:self.myTitle];
    self.title=NSLocalizedString(@"与会员交流", @"与会员交流");
    self.view.backgroundColor = UICOLOR(240, 239, 245, 1);
    
    //add UItableView
    self.tableView=[[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y,APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT-60) style:UITableViewStylePlain];
    [self.tableView registerClass:[ChartCell class] forCellReuseIdentifier:cellIdentifier];
    self.tableView.separatorStyle=UITableViewCellSeparatorStyleNone;
    self.tableView.allowsSelection = NO;
    //self.tableView.backgroundView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"chat_bg_default.jpg"]];
    self.tableView.backgroundColor = UICOLOR(240, 239, 245, 1);
    self.tableView.dataSource=self;
    self.tableView.delegate=self;
    [self.view addSubview:self.tableView];
    
    //add keyBorad
    
    self.keyBordView=[[KeyBordVIew alloc]initWithFrame:CGRectMake(0, self.view.frame.size.height-60, self.view.frame.size.width, 60)];
    self.keyBordView.delegate=self;
    [self.view addSubview:self.keyBordView];

    //初始化数据
    [self initwithData];
    
    self.tableViewRect = self.tableView.frame;
    self.keyboardRect = self.keyBordView.frame;
    
    [self httpContentRequest:NO];
    [self httpUnreadRequest];
    
    /*加载更多*/
    _refresh = [[UIRefreshControl alloc] init];
    _refresh.tintColor = [UIColor lightGrayColor];
    [_refresh addTarget:self action:@selector(refreshView:) forControlEvents:UIControlEventValueChanged];
    [self.tableView addSubview: _refresh];
}

-(void)handleData
{
    [_refresh endRefreshing];
    [self httpContentRequest:YES];
}


-(void)refreshView:(UIRefreshControl *)refresh
{
    if (refresh.refreshing) {
        [self.dataArray removeAllObjects];
        [self performSelector:@selector(handleData) withObject:nil afterDelay:2];
    }
}

-(void)initwithData
{

    self.cellFrames=[NSMutableArray array];
    self.dataArray=[NSMutableArray array];
//    {
//        content = "\U4f60\U5988B\U7684\Uff01\Uff01\Uff01\U6eda\Uff01\Uff01\Uff01";
//        icon = "icon02.jpg";
//        time = "\U6628\U5929 20:15";
//        type = 1;
//    }
//    NSString *path=[[NSBundle mainBundle] pathForResource:@"messages" ofType:@"plist"];
//    NSArray *data=[NSArray arrayWithContentsOfFile:path];

    if(self.model==nil)
        return;
    [self.dataArray addObject:@{@"content":[self.model objectForKey:@"text"],@"icon":[self.model objectForKey:@"userimg"],@"time":[self.model objectForKey:@"createdate"],@"type":@"0"}];
    
    [self refreshTableView];
}

- (void)refreshTableView
{
    if(self.cellFrames&&self.cellFrames.count>0)
        [self.cellFrames removeAllObjects];
    for(NSDictionary *dict in self.dataArray){
        
        ChartCellFrame *cellFrame=[[ChartCellFrame alloc]init];
        ChartMessage *chartMessage=[[ChartMessage alloc]init];
        chartMessage.dict=dict;
        cellFrame.chartMessage=chartMessage;
        [self.cellFrames addObject:cellFrame];
        
    }
    
    [self.tableView reloadData];
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *v = nil;
    if(section==0){
        v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.tableView.frame.size.width, 30)];
        v.backgroundColor = [UIColor clearColor];
        UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(25,0, 120,20)];
        label.textAlignment = NSTextAlignmentCenter;
        label.text = [self.model objectForKey:@"createdate"];
        label.numberOfLines = 0;
        [label.layer setMasksToBounds:YES];
        label.layer.borderColor = [[UIColor colorWithRed:215.0 / 255.0 green:215.0 / 255.0 blue:215.0 / 255.0 alpha:1] CGColor];
        label.layer.borderWidth = 0;
        label.layer.cornerRadius = 4.f;
        label.backgroundColor = [UIColor clearColor];
        label.font = [UIFont systemFontOfSize:13.f];
        label.textColor = [UIColor colorWithHexString:@"0xc7c7c9"];
        [label setCenter:CGPointMake(v.frame.size.width/2, v.frame.size.height/2)];
        [v addSubview:label];
    }
    return v;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0.f;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.cellFrames.count;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    ChartCell *cell=[tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    cell.delegate=self;
    cell.cellFrame=self.cellFrames[indexPath.row];
    return cell;

}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return [self.cellFrames[indexPath.row] cellHeight];
}
-(void)chartCell:(ChartCell *)chartCell tapContent:(NSString *)content
{
    if(self.player.isPlaying){
    
        [self.player stop];
    }
    //播放
    NSString *filePath=[NSString documentPathWith:content];
    NSURL *fileUrl=[NSURL fileURLWithPath:filePath];
    [self initPlayer];
    NSError *error;
    self.player=[[AVAudioPlayer alloc]initWithContentsOfURL:fileUrl error:&error];
    [self.player setVolume:1];
    [self.player prepareToPlay];
    [self.player setDelegate:self];
    [self.player play];
    [[UIDevice currentDevice] setProximityMonitoringEnabled:YES];
}
-(void)audioPlayerDidFinishPlaying:(AVAudioPlayer *)player successfully:(BOOL)flag
{
    [[UIDevice currentDevice]setProximityMonitoringEnabled:NO];
    [self.player stop];
    self.player=nil;
}
-(void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{

    [self.view endEditing:YES];
}
-(void)KeyBordView:(KeyBordVIew *)keyBoardView textFiledReturn:(UITextField *)textFiled
{
    ChartCellFrame *cellFrame=[[ChartCellFrame alloc]init];
    ChartMessage *chartMessage=[[ChartMessage alloc]init];
    

//   chartMessage.icon = _icon_url;
    chartMessage.time = [gloabFunction getNowDateAndTime];
    chartMessage.icon = userImgurl;
    
    NSString *str2 = [textFiled.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    chartMessage.content=str2;
    
    if(!self.boptype||[self.boptype isEqualToString:@"none"]){
        if(!str2||str2.length==0)
        {
            CSAlert(NSLocalizedString(@"请输入内容", @"请输入内容"));
            return;
        }
        
        [self httpRequest:str2];
        chartMessage.messageType=1;
    }
    
    cellFrame.chartMessage=chartMessage;
    
    [self.cellFrames addObject:cellFrame];
    [self.tableView reloadData];
    
    //滚动到当前行
    
    [self tableViewScrollCurrentIndexPath];
    textFiled.text=@"";


}
-(void)KeyBordView:(KeyBordVIew *)keyBoardView textFiledBegin:(UITextField *)textFiled
{
    [self tableViewScrollCurrentIndexPath];

}
-(void)beginRecord
{
    if(self.recording)return;
    
    self.recording=YES;
    
    NSDictionary *settings=[NSDictionary dictionaryWithObjectsAndKeys:
                            [NSNumber numberWithFloat:8000],AVSampleRateKey,
                            [NSNumber numberWithInt:kAudioFormatLinearPCM],AVFormatIDKey,
                            [NSNumber numberWithInt:1],AVNumberOfChannelsKey,
                            [NSNumber numberWithInt:16],AVLinearPCMBitDepthKey,
                            [NSNumber numberWithBool:NO],AVLinearPCMIsBigEndianKey,
                            [NSNumber numberWithBool:NO],AVLinearPCMIsFloatKey,
                            nil];
    [[AVAudioSession sharedInstance] setCategory: AVAudioSessionCategoryPlayAndRecord error: nil];
    [[AVAudioSession sharedInstance] setActive:YES error:nil];
    NSDate *now = [NSDate date];
    NSDateFormatter *dateFormater = [[NSDateFormatter alloc] init];
    [dateFormater setDateFormat:@"yyyyMMddHHmmss"];
    NSString *fileName = [NSString stringWithFormat:@"rec_%@.wav",[dateFormater stringFromDate:now]];
    self.fileName=fileName;
    NSString *filePath=[NSString documentPathWith:fileName];
    NSURL *fileUrl=[NSURL fileURLWithPath:filePath];
    NSError *error;
    self.recorder=[[AVAudioRecorder alloc]initWithURL:fileUrl settings:settings error:&error];
    [self.recorder prepareToRecord];
    [self.recorder setMeteringEnabled:YES];
    [self.recorder peakPowerForChannel:0];
    [self.recorder record];

}
-(void)finishRecord
{
    self.recording=NO;
    [self.recorder stop];
    self.recorder=nil;
    ChartCellFrame *cellFrame=[[ChartCellFrame alloc]init];
    ChartMessage *chartMessage=[[ChartMessage alloc]init];
    
    int random=arc4random_uniform(2);
    NSLog(@"%d",random);
    chartMessage.icon=[NSString stringWithFormat:@"icon%02d.jpg",random+1];
    chartMessage.messageType=random;
    chartMessage.content=self.fileName;
    cellFrame.chartMessage=chartMessage;
    [self.cellFrames addObject:cellFrame];
    [self.tableView reloadData];
    [self tableViewScrollCurrentIndexPath];

}
-(void)tableViewScrollCurrentIndexPath
{
    if(self.cellFrames.count<1){
        [self.tableView scrollsToTop];
        return;
    }
    NSIndexPath *indexPath=[NSIndexPath indexPathForRow:self.cellFrames.count-1 inSection:0];
    [self.tableView scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewScrollPositionBottom animated:YES];
}

- (void)scrollViewToBottom:(BOOL)animated
{
    if (self.tableView.contentSize.height > self.tableView.frame.size.height)
    {
        CGPoint offset = CGPointMake(0, self.tableView.contentSize.height - self.tableView.frame.size.height);
        [self.tableView setContentOffset:offset animated:YES];
    }
}

-(void)initPlayer{
    //初始化播放器的时候如下设置
    UInt32 sessionCategory = kAudioSessionCategory_MediaPlayback;
    AudioSessionSetProperty(kAudioSessionProperty_AudioCategory,
                            sizeof(sessionCategory),
                            &sessionCategory);
    
    UInt32 audioRouteOverride = kAudioSessionOverrideAudioRoute_Speaker;
    AudioSessionSetProperty (kAudioSessionProperty_OverrideAudioRoute,
                             sizeof (audioRouteOverride),
                             &audioRouteOverride);
    
    AVAudioSession *audioSession = [AVAudioSession sharedInstance];
    //默认情况下扬声器播放
    [audioSession setCategory:AVAudioSessionCategoryPlayback error:nil];
    [audioSession setActive:YES error:nil];
    audioSession = nil;
}

/**
 *  回答问题
 *
 *  @param str 回复内容
 */
- (void)httpRequest:(NSString *)str
{
    [self.view endEditing: YES];
    /*
    
    NSString* vcode = [gloabFunction getSign:@"sendMsg" strParams:[gloabFunction getShopCode]];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dic = @{@"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"shopCode":self.shopID,
                          @"userCode":[gloabFunction getUserCode],
                          @"staffCode":[gloabFunction getStaffCode],
                          @"message":str,
                          @"reqtime": [gloabFunction getTimestamp]
                          };
    [self initJsonPrcClient:@"2"];
    [self.jsonPrcClient invokeMethod:@"sendMsg" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){
            
            if([[responseObject objectForKey:@"code"] intValue]==50000){
                //CSAlert(@"发送成功");
            }else{
                [SVProgressHUD showErrorWithStatus:@"发送失败"];
            }
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];

    */
    
//    NSString* vcode = [gloabFunction getSign:@"sendMsg" strParams:[gloabFunction getUserCode]];
//    NSDictionary *dic = @{@"tokenCode":[gloabFunction getUserToken],
//                          @"vcode":vcode,
//                          @"shopCode":self.shopID,
//                          @"userCode":[gloabFunction getUserCode],
//                          @"staffCode":[gloabFunction getStaffCode],
//                          @"message":str,
//                          @"reqtime": [gloabFunction getTimestamp]
//                          };
    
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.shopID forKey:@"shopCode"];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[NSString stringWithFormat:@"%@",str] forKey:@"message"];

    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
     NSString* vcode = [gloabFunction getSign:@"sendMsg" strParams:self.shopID];
    [params setObject:vcode forKey:@"vcode"];
    
    
    
    [self.jsonPrcClient invokeMethod:@"sendMsg" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){
            
            if([[responseObject objectForKey:@"code"] intValue]==50000){
                //CSAlert(@"发送成功");
                
                //初始化数据
//                [self initwithData];
                
//                [self.tableView reloadData];
                
            }else{
                [SVProgressHUD showErrorWithStatus:@"发送失败"];
            }
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"%@",error);
        [SVProgressHUD showErrorWithStatus:@"发送失败"];
    }];
    
    
    
}

- (void)httpContentRequest:(BOOL)isAll
{
    /*
    
    NSString *allStr = @"";
    if(isAll)
        allStr = @"1";
    else
        allStr = @"";

    NSString* vcode = [gloabFunction getSign:@"getMsg" strParams:[gloabFunction getUserCode]];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dic = @{@"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"shopCode":self.shopID,
                          @"userCode":[gloabFunction getUserCode],
                          @"all":allStr,
                          @"reqtime": [gloabFunction getTimestamp]
                          };
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"getMsg" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){

            if(((NSArray *)[responseObject objectForKey:@"msgList"]).count>0){
                NSUInteger count = ((NSArray *)[responseObject objectForKey:@"msgList"]).count;
                //for(int i=count-1;i>=0;i--){
                for(NSDictionary *objDic in [responseObject objectForKey:@"msgList"]){
                    //NSDictionary *objDic = [[responseObject objectForKey:@"msgList"] objectAtIndex:i];
                    NSString *type=@"";
                    NSString *icon=@"";
                    NSString *message=[objDic objectForKey:@"message"]?[objDic objectForKey:@"message"]:@"";
*/
                    /*from_where == 0 商家 type==1 右边*/
    /*
                    if([[objDic objectForKey:@"from_where"] intValue] == 0){
                        type=@"1";
                        icon=[objDic objectForKey:@"logoUrl"]?[objDic objectForKey:@"logoUrl"]:@"";
                    }else{
                        type=@"0";
                        icon=[objDic objectForKey:@"userAvatar"]?[objDic objectForKey:@"userAvatar"]:@"";
                    }
                    NSDictionary *dicTemp = @{
                                              @"content":message,
                                              @"icon":icon,
                                              @"time":[objDic objectForKey:@"createTime"],
                                              @"type":type};
                    
                    [self.dataArray addObject:dicTemp];
            
                }
                [self refreshTableView];   
            }
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        CSAlert(error.localizedDescription);
    }];
*/
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.shopID forKey:@"shopCode"];
    [params setObject:@"1" forKey:@"all"];
   
    NSString* vcode = [gloabFunction getSign:@"getMsg" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [SVProgressHUD showWithStatus:@"正在加载"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"getMsg" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        
        
        if(((NSArray *)[responseObject objectForKey:@"msgList"]).count>0){
            
            NSUInteger count = ((NSArray *)[responseObject objectForKey:@"msgList"]).count;
            //for(int i=count-1;i>=0;i--){
            for(NSDictionary *objDic in [responseObject objectForKey:@"msgList"]){
                //NSDictionary *objDic = [[responseObject objectForKey:@"msgList"] objectAtIndex:i];
                NSString *type=@"";
                NSString *icon=@"";
                NSString *message=[objDic objectForKey:@"message"]?[objDic objectForKey:@"message"]:@"";
                
                /*from_where == 0 商家 type==1 右边*/
                if([[objDic objectForKey:@"from_where"] intValue] == 0){
                    type=@"0";
                    icon=[objDic objectForKey:@"logoUrl"]?[objDic objectForKey:@"logoUrl"]:@"";
                }else{
                    type=@"1";
                    icon=[objDic objectForKey:@"userAvatar"]?[objDic objectForKey:@"userAvatar"]:@"";
                }
                NSDictionary *dicTemp = @{
                                          @"content":message,
                                          @"icon":icon,
                                          @"time":[objDic objectForKey:@"createTime"],
                                          @"type":type
                                          };
                
                [wself.dataArray addObject:dicTemp];
                
            }
            [wself refreshTableView];
        }
 
     
     
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD showErrorWithStatus:@"加载失败，请查看网络"];
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"error" message:@"聊天加载失败" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
        [alterView show];
        
        
    }];

    
    
}

- (void)httpUnreadRequest
{
    NSString* vcode = [gloabFunction getSign:@"countUnreadMsg" strParams:[gloabFunction getUserCode]];
    
    if([gloabFunction getUserCode].length==0||[gloabFunction getUserCode]==nil)
        return;
    NSDictionary *dic = @{@"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"shopCode":self.shopID,
                          @"userCode":[gloabFunction getUserCode],
                          @"reqtime": [gloabFunction getTimestamp]
                          };
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"countUnreadMsg" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if(IsNOTNullOrEmptyOfNSString(responseObject)){
            _unRead = (NSString *)responseObject;
            if([_unRead intValue]==0){
                [self httpReadRequest];
            }
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];
}

- (void)httpReadRequest
{
    NSString* vcode = [gloabFunction getSign:@"readMsg" strParams:[gloabFunction getUserCode]];
    
    if([gloabFunction getUserCode].length==0||[gloabFunction getUserCode]==nil)
        return;
    NSDictionary *dic = @{@"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"shopCode":self.shopID,
                          @"userCode":[gloabFunction getUserCode],
                          @"reqtime": [gloabFunction getTimestamp]
                          };
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"readMsg" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){
            if([[responseObject objectForKey:@"code"] intValue]==50000){
                NSLog(@"success Read");
            }
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];
}

- (void)popRefreshData:(PopRefreshData)block
{
    self.pop = block;
}
-(void)btnBackClicked:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
    self.pop(nil);
}

- (void)getUserInfo
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    NSString* vcode = [gloabFunction getSign:@"getUserInfo" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getUserInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        
        userImgurl = [responseObject  objectForKey:@"avatarUrl"];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
}



@end
