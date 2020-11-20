//
//  NewActivityViewController.m
//  BMSQS
//
//  Created by gh on 15/12/28.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "NewActivityViewController.h"
#import "ChargeViewController.h"
#import "ActivityTypeViewController.h"
#import "NewActivityIntroduceViewController.h"
#import "SVProgressHUD.h"
#import "UIImageView+WebCache.h"
#import "NewSecondActivityCell.h"
#import "NewTopActivityCell.h"
#import "DAProgressOverlayView.h"
#import "AFURLSessionManager.h"
#import "VPImageCropperViewController.h"
#import "TableViewWithBlock.h"
#import "WMCustomDatePicker.h"
#import "NewBottomActivityCell.h"

#import "ActImageTextAttachment.h"

#define ORIGINAL_MAX_WIDTH 640.0f

@interface NewActivityViewController () <UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate, ActivityDelegate, TopActivityDelegate, UINavigationControllerDelegate, UIImagePickerControllerDelegate, VPImageCropperDelegate, WMCustomDatePickerDelegate, UIActionSheetDelegate>
{
    UIView *pickerView;
}

//tableview
@property (nonatomic, strong)TableViewWithBlock *refundTableView;


@property (nonatomic, assign)BOOL isShowAdvanced;
@property (nonatomic, strong)UITableView *tableView;

@property (nonatomic, strong)NSString *activityImg;     // 活动图片
@property (nonatomic, strong)NSString *activityName;    //活动名称
@property (nonatomic, strong)NSString *startTime;       //开始时间
@property (nonatomic, strong)NSString *endTime;         //活动结束时间
@property (nonatomic, strong)NSString *startTimeText;
@property (nonatomic, strong)NSString *endTimeText;
@property (nonatomic, strong)NSString *activityLocation; //地址
@property (nonatomic, strong)NSString *txtContent;       //活动说明
//活动类型
@property (nonatomic, strong)NSArray *actTypeArray;
@property (nonatomic, assign)int actType;               //活动类型 1-聚会，2-运动，3-户外，4-亲子，5-体验课，6-音乐
@property (nonatomic, strong)NSString *actTypeName ;

@property (nonatomic, strong)NSString *contactName;     //联系人
@property (nonatomic, strong)NSString *contactMobileNbr; //联系电话
@property (nonatomic, strong)NSMutableArray *feeScaleArray; //收费规格
@property (nonatomic, strong)NSArray *refundRequiredArray;
@property (nonatomic, strong)NSString *refundRequired;  //退款要求
@property (nonatomic, assign)int refundRequiredID;      //需要传的退款要求
@property (nonatomic, strong)NSString *dayOfBooking;    // 提前预约天数
@property (nonatomic, strong)NSString *registerNbrRequired;     //单人报名人数限制
@property (nonatomic, strong)NSString *limitedParticipators;    //活动参与人数限制

@property (nonatomic, strong)UILabel *textViewPlaceholder; //textView  placeholder

@property (nonatomic, assign)int clickDatePicker; //时间

@property (nonatomic, strong)UIAlertController *alertController; //actionsheet

@property (nonatomic, strong)UITextView *actTextView; 
//价格区间
@property (nonatomic, strong)NSNumber * maxPrice;   //最高金额
@property (nonatomic, strong)NSNumber * minPrice;   //最低金额


@property (nonatomic, assign)NSInteger tag;

@property (nonatomic, assign)CGSize textActSize;




@end


@implementation NewActivityViewController

- (id)init {
    self = [super init];
    if (self) {
        self.feeScaleArray = [[NSMutableArray alloc] init]; //活动收费
    }
    return self;
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSString *titleStr;
    if (self.isEditActivity) {
        titleStr = @"编辑活动";
    }else {
        titleStr = @"新建活动";
    }
    [self setNavTitle:titleStr];
    
    [self setNavBackItem];
    [self setViewUp];

    
}

- (void)setViewUp {
    
    
    //Add keyboard notification
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onKeyboardNotification:) name:UIKeyboardWillHideNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onKeyboardNotification:) name:UIKeyboardWillShowNotification object:nil];
    
    if (IOS8) {
        self.alertController = [UIAlertController alertControllerWithTitle:nil
                                                                   message:nil
                                                            preferredStyle:UIAlertControllerStyleActionSheet];
        UIAlertAction* cancelAction = [UIAlertAction actionWithTitle:@"取消"
                                                               style:UIAlertActionStyleCancel
                                                             handler:nil];
        
        UIAlertAction* fromPhotoAction = [UIAlertAction actionWithTitle:@"从相册选择"
                                                                  style:UIAlertActionStyleDefault
                                                                handler:^(UIAlertAction * action) {
                                                                    
                                                                    UIImagePickerController *controller = [[UIImagePickerController alloc] init];
                                                                    controller.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
                                                                    NSMutableArray *mediaTypes = [[NSMutableArray alloc] init];
                                                                    [mediaTypes addObject:(__bridge NSString *)kUTTypeImage];
                                                                    controller.mediaTypes = mediaTypes;
                                                                    controller.delegate = self;
                                                                    [self presentViewController:controller
                                                                                       animated:YES
                                                                                     completion:^(void){
                                                                                         NSLog(@"Picker View Controller is presented");
                                                                                     }];
                                                                    
                                                                    
                                                                }];
        
        [self.alertController addAction:cancelAction];
        [self.alertController addAction:fromPhotoAction];
    
    }else {
    
    }
    
    
    
    //通知刷新 活动类型
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(callReload:)
                                                 name:@"newTopActivityType"
                                               object:nil];
    


    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(callReloadPrice:)
                                                 name:@"changeViewController"
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(txtContentFinish:)
                                                name:@"txtContentFinish"
                                              object:nil];
    
    
    self.actType = -1;
    self.activityName = @"";
    self.activityLocation = @"";
    self.contactName = @"";
    self.contactMobileNbr = @"";
    self.isShowAdvanced = NO; //默认关闭高级设置
    self.txtContent = @"";
    self.dayOfBooking = @"";
    self.registerNbrRequired = @"";
    self.limitedParticipators = @"";
    self.refundRequiredID = 0;
    self.endTime = @"";
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 64) style:UITableViewStylePlain];
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.keyboardDismissMode  = UIScrollViewKeyboardDismissModeInteractive;
    [self.view addSubview:self.tableView];
    
    //活动介绍
    self.actTextView = [[UITextView alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 100)];
//    self.actTextView.editable = NO;
    self.actTextView.layer.borderColor = [UIColor grayColor].CGColor;
    self.actTextView.layer.borderWidth =1.0;
    self.actTextView.layer.cornerRadius =5.0;
    self.actTextView.backgroundColor = [UIColor whiteColor];
    
    UIButton *btn2 = [UIButton buttonWithType:UIButtonTypeCustom];
    btn2.frame = self.actTextView.frame;
    btn2.tag = 30010;
    [btn2 addTarget:self action:@selector(editActView) forControlEvents:UIControlEventTouchUpInside];
    [self.actTextView addSubview:btn2];
    
    self.textViewPlaceholder = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 20)];
    self.textViewPlaceholder.font = [UIFont systemFontOfSize:13.f];
    self.textViewPlaceholder.textColor = UICOLOR(205, 205, 209, 1.0);
    self.textViewPlaceholder.text = @"请输入活动详情介绍";
    [self.actTextView addSubview:self.textViewPlaceholder];

    [self setUpDateView];
    
    [self getActRefundChoice]; //退款模式
    [self getActType]; //活动类型
    
    if (self.isEditActivity){ // 是否是编辑活动
        [self getActivityInfo];
        
    }
    
    
}

#pragma mark - 将txtcontent字符串转换为数组txtContent
- (NSArray *)arrayWithTxtContent:(NSString *)string {
    NSMutableString *mutableString = [[NSMutableString alloc] initWithString:string];
    while ([mutableString rangeOfString:@"<img src="].length > 0 || [mutableString rangeOfString:@"\"/><BR />"].length > 0) {
        
        [mutableString deleteCharactersInRange:[mutableString rangeOfString:@"<img src="]];
        [mutableString deleteCharactersInRange:[mutableString rangeOfString:@"/><BR />"]];
        NSLog(@"%@",mutableString);
        
    }
    
    NSLog(@"%@",mutableString);
    
    NSArray *array = [mutableString componentsSeparatedByCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@"\""]]; //从字符A中分隔成2个元素的数组
    
    NSLog(@"%@",array);
    return array;
}


- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"newTopActivityType"
                                                  object:nil];//object 与注册时相同
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"changeViewController"
                                                  object:nil];//object 与注册时相同
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"txtContentFinish"
                                                  object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification
                                                  object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification
                                                  object:nil];

}

- (void)btnActivity {
    
    if (self.activityName.length == 0 || self.startTime.length == 0 || self.activityLocation.length == 0 ||self.actType == 0 || self.contactName.length == 0 || self.contactMobileNbr.length == 0 || self.activityImg.length == 0) {
        CSAlert(@"请将信息填写完整");
        return;
    }
    
    
    
    if (self.isEditActivity) {
        [self editActivity]; //编辑活动
    }else {
        [self addActivity]; //添加活动
    }
    
    
}


- (void)addActivity {
   

    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:self.activityName forKey:@"activityName"];      //活动主题
    [params setObject:self.startTime forKey:@"startTime"];         //开始时间
    [params setObject:self.endTime forKey:@"endTime"];           //结束时间
    [params setObject:self.activityLocation forKey:@"activityLocation"];  //活动地点
    [params setObject:self.txtContent forKey:@"txtContent"];        //活动说明
    
    
    [params setObject:self.activityImg forKey:@"activityImg"];       //活动图片
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"]; //发起活动的商家编码
    [params setObject:[gloabFunction getStaffCode] forKey:@"creatorCode"];       //活动发起人编码
   
    [params setObject:[NSNumber numberWithInt:self.actType]  forKey:@"actType"];//活动类型 1-聚会，2-运动，3-户外，4-亲子，5-体验课，6-音乐
    
    [params setObject:self.contactName forKey:@"contactName"];       // 联系人
    [params setObject:self.contactMobileNbr forKey:@"contactMobileNbr"];  //联系方式
    
    NSString *jsonStr = [self jsonStringWithArray:self.feeScaleArray];
    [params setObject:jsonStr forKey:@"feeScale"];          // 收费规格
    
    [params setObject:[NSNumber numberWithInt:self.refundRequiredID] forKey:@"refundRequired"];    // 退款要求
    [params setObject:self.dayOfBooking forKey:@"dayOfBooking"];      //  提前预约天数
    [params setObject:self.registerNbrRequired forKey:@"registerNbrRequired"]; // 单人报名人数限制
    [params setObject:self.limitedParticipators forKey:@"limitedParticipators"];     //活动参与人数上限
    
    
    [params setObject:@"" forKey:@"activityLogo"];       //活动方形图片
    [params setObject:@"0" forKey:@"isPrepayRequired"];  //是否需要预付费。1-需要；0-不需要
    [params setObject:@"0" forKey:@"prePayment"];        //预付金额
    [params setObject:@"0" forKey:@"isRegisterRequired"];//是否需要报名。1-需要；0-不需要
    [params setObject:@"3" forKey:@"activityBelonging"]; //活动归属 1-平台活动；2-工行活动；3-商家活动
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"addActivity" strParams:self.activityName];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"addActivity" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        
        if (code.intValue == 50000) {
            [weakSelf.navigationController popViewControllerAnimated:YES];
            
        } else {
            [weakSelf errorMessageCode:code.intValue];
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        
    }];

    
    
}



- (void)editActivity {
    
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    
    NSMutableDictionary *updateDataDic = [[NSMutableDictionary alloc]init];
    
    
    [updateDataDic setObject:self.activityCode forKey:@"activityCode"];
    [updateDataDic setObject:self.activityName forKey:@"activityName"];      //活动主题
    [updateDataDic setObject:self.startTime forKey:@"startTime"];         //开始时间
    [updateDataDic setObject:self.endTime forKey:@"endTime"];           //结束时间
    [updateDataDic setObject:self.activityLocation forKey:@"activityLocation"];  //活动地点
    [updateDataDic setObject:self.txtContent forKey:@"richTextContent"];        //活动说明
    [updateDataDic setObject:self.limitedParticipators forKey:@"limitedParticipators"];     //活动参与人数上限
    [updateDataDic setObject:self.activityImg forKey:@"activityImg"];       //活动图片
    [updateDataDic setObject:[gloabFunction getShopCode] forKey:@"shopCode"]; //发起活动的商家编码
    [updateDataDic setObject:[gloabFunction getStaffCode] forKey:@"creatorCode"];       //活动发起人编码
    [updateDataDic setObject:[NSNumber numberWithInt:self.actType]  forKey:@"actType"];//活动类型 1-聚会，2-运动，3-户外，4-亲子，5-体验课，6-音乐
    [updateDataDic setObject:self.contactName forKey:@"contactName"];       // 联系人
    [updateDataDic setObject:self.contactMobileNbr forKey:@"contactMobileNbr"];  //联系方式
    NSString *jsonStr = [self jsonStringWithArray:self.feeScaleArray];
    [updateDataDic setObject:jsonStr forKey:@"feeScale"];          // 收费规格
    [updateDataDic setObject:[NSNumber numberWithInt:self.refundRequiredID] forKey:@"refundRequired"];//退款要求
    [updateDataDic setObject:self.registerNbrRequired forKey:@"registerNbrRequired"]; // 单人报名人数限制
    [updateDataDic setObject:self.dayOfBooking forKey:@"dayOfBooking"];      //  提前预约天数
    
    
    
//    [updateDataDic setObject:@"" forKey:@"activityLogo"];       //活动方形图片
//    [updateDataDic setObject:@"0" forKey:@"isPrepayRequired"];  //是否需要预付费。1-需要；0-不需要
//    [updateDataDic setObject:@"0" forKey:@"prePayment"];        //预付金额
//    [updateDataDic setObject:@"0" forKey:@"isRegisterRequired"];//是否需要报名。1-需要；0-不需要
//    [updateDataDic setObject:@"3" forKey:@"activityBelonging"]; //活动归属 1-平台活动；2-工行活动；3-商家活动
    
//    NSArray *array = [NSArray arrayWithObject:updateDataDic];
    
    
    NSString *updateJsonStr = [self jsonStringWithDictionary:updateDataDic];
    
    [params setObject:updateJsonStr forKey:@"updateData"];
    [params setObject:self.activityCode forKey:@"activityCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"editActivity" strParams:self.activityCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"editActivity" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        
        if (code.intValue == 50000) {
            [weakSelf.navigationController popViewControllerAnimated:YES];
            
        } else {
            [weakSelf errorMessageCode:code.intValue];
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        
    }];

    
}



- (void)errorMessageCode:(int)code {

    
   
    switch (code) {
        case 20000:
            CSAlert(@"失败，请重试");
            break;
        case 50200:
            CSAlert(@"活动主题不正确");
            break;
        case 50201:
            CSAlert(@"活动开始时间不正确");
            break;
        case 50202:
            CSAlert(@"活动结束时间不正确");
            break;
        case 50203:
            CSAlert(@"活动地点不正确");
            break;
        case 50204:
            CSAlert(@"活动说明不正确");
            break;
        case 50205:
            CSAlert(@"活动参与人数上限不正确");
            break;
        case 50206:
            CSAlert(@"是否需要预付费不正确");
            break;
        case 50207:
            CSAlert(@"预付金额不正确");
            break;
        case 50208:
            CSAlert(@"是否需要报名不正确");
            break;
        case 50209:
            CSAlert(@"活动图片不正确");
            break;
        case 50210:
            CSAlert(@"活动方形图片不正确");
            break;
        case 50314:
            CSAlert(@"商家编码不正确");
            break;
        case 50212:
            CSAlert(@"活动发起人编码不正确");
            break;
        case 50213:
            CSAlert(@"活动归属不正确");
            break;
        case 50214:
            CSAlert(@"活动开始时间不能大于活动结束时间");
            break;
        default:
            break;
    }
    
}


#pragma mark - 创建时间选择器
- (void)setUpDateView {
    
    pickerView   = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_HEIGHT-320, [UIScreen mainScreen].bounds.size.width, 300)];
    pickerView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:pickerView];
    [self.view bringSubviewToFront:pickerView];
    pickerView.hidden = YES;
    
    WMCustomDatePicker *picker = [[WMCustomDatePicker alloc]initWithframe:CGRectMake(0, 20, [UIScreen mainScreen].bounds.size.width, 300) Delegate:self PickerStyle:WMDateStyle_YearMonthDayHourMinute];
    picker.minLimitDate = [NSDate date];
    picker.maxLimitDate = [NSDate dateWithTimeIntervalSinceNow:24*60*60*30*12];
    [pickerView  addSubview:picker];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, 20);
    [button addTarget:self action:@selector(btnPickerFinish) forControlEvents:UIControlEventTouchUpInside];
    button.backgroundColor = [UIColor whiteColor];
    [button setTitle:@"完成" forState:UIControlStateNormal];
    [button setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [pickerView addSubview:button];
    
}

- (void)btnPickerFinish {
    NSLog(@"完成");
    pickerView.hidden = YES;
}


#pragma mark - WMCustomDatePickerDelegate
- (void)finishDidSelectDatePicker:(WMCustomDatePicker *)datePicker year:(NSString *)year month:(NSString *)month day:(NSString *)day hour:(NSString *)hour minute:(NSString *)minute weekDay:(NSString *)weekDay
{
    NSLog(@"%@====%@=====%@=====%@=====%@=====%@=====",year,month,day,hour,minute,weekDay);
    //年月日时分[NSString stringWithFormat:@"%@%@%@%@%@",year,month,day,hour,minute];
//    NewTopActivityCell *cell = [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:0 inSection:1]];
    if (self.clickDatePicker == 1 ) {
//        [NSString stringWithFormat:@"%@%@%@%@%@",year,month,day,hour,minute];
//        cell.label3.textColor = UICOLOR(51, 51, 51, 1);
        
    }else if (self.clickDatePicker == 2) {
//        [NSString stringWithFormat:@"%@%@%@%@%@",year,month,day,hour,minute];
//        cell.label4.textColor = UICOLOR(51, 51, 51, 1);
    }

    
    
    
    
}

- (void)finishDidSelectDatePicker:(WMCustomDatePicker *)datePicker date:(NSDate *)date
{
    NSLog(@"%@_____formatterDate == %@",date,[self dateFromString:date withFormat:@"yyyy-MM-dd HH:mm"]);

    if (self.clickDatePicker == 1 ) {
        self.startTime = [self dateFromString:date withFormat:@"yyyy-MM-dd HH:mm"];
    }else if (self.clickDatePicker == 2) {
        self.endTime = [self dateFromString:date withFormat:@"yyyy-MM-dd HH:mm"];
    }
    
    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:1];
    [self.tableView  reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
    
    
}

//根据date返回string
- (NSString *)dateFromString:(NSDate *)date withFormat:(NSString *)format {
    NSDateFormatter *inputFormatter = [[NSDateFormatter alloc] init];
    [inputFormatter setDateFormat:format];
    NSString *dateStr = [inputFormatter stringFromDate:date];
    return dateStr;
    
}




#pragma mark - 退款
//请求退款模式
- (void)getActRefundChoice {
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@""];

    [self.jsonPrcClient invokeMethod:@"getActRefundChoice" withParameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
//        NSLog(@"%@",responseObject);
        self.refundRequiredArray = responseObject;
        [self setUpRefundTableView:responseObject];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];

    
    
}

//退款列表
- (void)setUpRefundTableView:(NSArray *)array {
//    NSArray *array = @[@"当天不退", @"24h不退", @"随意退、过期退", @"48H不退"];
    self.refundTableView  = [[TableViewWithBlock alloc] init];
    self.refundTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.refundTableView initTableViewDataSourceAndDelegate:^NSInteger(UITableView *tableView, NSInteger section) {
        return array.count + 1;
    } setCellForIndexPathBlock:^UITableViewCell *(UITableView *tableView, NSIndexPath *indexPath) {
        
        UITableViewCell *cell=[tableView dequeueReusableCellWithIdentifier:@"SelectionCell"];
        if (!cell) {
            cell=[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"SelectionCell"];
            [cell setSelectionStyle:UITableViewCellSelectionStyleGray];
            cell.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/3*2, 20);

        }
        if (indexPath.row == 0) {
            CGRect frame = cell.frame;
            frame.size.height = 0;
            cell.frame = frame;
        }
        
        for (NSDictionary *dic  in array) {
            NSString *value = [dic objectForKey:@"refundValue"];
            if (indexPath.row == value.intValue) {
                [cell.textLabel setText:[NSString stringWithFormat:@"%@",dic[@"refundName"]]];
            }
        }
        
//        [cell.textLabel setText:[NSString stringWithFormat:@"%@",array[indexPath.row]]];
//        cell.textLabel.frame = CGRectMake(0, cell.textLabel.frame.origin.y, cell.textLabel.frame.size.width, cell.textLabel.frame.size.height);
        cell.textLabel.textColor = UICOLOR(51, 51, 51, 1);
        cell.textLabel.font = [UIFont systemFontOfSize:11.f];
        return cell;
        
    } setDidSelectRowBlock:^(UITableView *tableView, NSIndexPath *indexPath) {
        self.refundRequired = [array[indexPath.row-1] objectForKey:@"refundName"];
        self.refundRequiredID = [[array[indexPath.row-1] objectForKey:@"refundValue"] intValue];
        if (self.refundTableView.hidden == NO) {
            self.refundTableView.hidden = YES;
        }
        NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:2];
        [self.tableView  reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
        
    }];
    self.refundTableView.backgroundColor  = [UIColor clearColor];
    self.refundTableView.hidden = YES;
    
    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:2];
    [self.tableView  reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
    
    
}





#pragma mark - UITableView delegate
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.section == 0) {
        return APP_VIEW_WIDTH * 32.0/75.0;
    }
    
    if (indexPath.section == 1) {
        return 45*7;
    }
    if (indexPath.section == 2) {
        if (self.isShowAdvanced) {
            return 45*6;
        }else {
            return 0;
        }
        
    }
    if (indexPath.section == 3) {
        
        if (self.txtContent.length>0) {
            self.textViewPlaceholder.hidden = YES;
            
            return self.textActSize.height + 50;
            
        }else {
            return 150;
            
        }

    }

    return 0;
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 4;
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    
    
    if (section == 1 || section == 0) {
        return 1;
    }
    
    
    if (section == 2) {
        if (self.isShowAdvanced) {
            return 1;
        }else {
            return 0;
        }
        
        
    }
    if (section == 3) {
        return 1;
    }
    

    return 0;

}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {

//
//    
    if (indexPath.section == 0) { //活动图片
        NSString *identify = @"activityTopImage";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identify];
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identify];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.contentView.backgroundColor = UICOLOR(230, 230, 230, 1);
            CGFloat cellHeight = APP_VIEW_WIDTH * (32.0/75.0);
            
            
            
            UILabel *label = [[UILabel alloc ] initWithFrame:CGRectMake(0, cellHeight / 3, APP_VIEW_WIDTH, cellHeight/6)];
            label.textAlignment = NSTextAlignmentCenter;
            label.text = @"上传封面照片";
            label.textColor = UICOLOR(102, 102, 102, 1);
            
            
            
            
            UILabel *label2 = [[UILabel alloc ] initWithFrame:CGRectMake(0, cellHeight / 3 + cellHeight/6, APP_VIEW_WIDTH, cellHeight/6)];
            label2.font = [UIFont systemFontOfSize:13.f];
            label2.textAlignment = NSTextAlignmentCenter;
            label2.text = @"建议使用横向图片";
            label2.textColor = UICOLOR(102, 102, 102, 1);
            
            [cell.contentView addSubview:label];
            [cell.contentView addSubview:label2];
            
            
            UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, cellHeight)];
            imageView.backgroundColor = [UIColor clearColor];
            imageView.tag = 9001;
            [cell.contentView addSubview:imageView];
            
            
        }
        
        UIImageView *imageView = [cell viewWithTag:9001];
        
        if (_activityImg.length > 0){
            [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL, _activityImg]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
            
        }
        
        return cell;
        
    }
    else if (indexPath.section == 1) { // 活动标题 活动详细地址 活动开始时间 结束时间 活动类型 联系人 联系电话
        NSString *identify =@"TopActivityCell";
        NewTopActivityCell *topCell = (NewTopActivityCell *)[tableView dequeueReusableCellWithIdentifier:identify];
        
        if (topCell == nil) {
            topCell = [[NewTopActivityCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identify];
            
        }
        topCell.topActivityDelegate = self;
        topCell.selectionStyle = UITableViewCellSelectionStyleNone;
//      cell.backgroundColor = [UIColor clearColor];
        
        if (self.startTime.length > 0 ){
            topCell.label3.text = self.startTime;
            topCell.label3.textColor = UICOLOR(51, 51, 51, 1);
        }
        if (self.endTime.length > 0) {
            topCell.label4.text = self.endTime;
            topCell.label4.textColor = UICOLOR(51, 51, 51, 1);
        }
        

        topCell.textField1.text = self.activityName;
        topCell.textField2.text = self.activityLocation;
        topCell.textField6.text = self.contactName;
        topCell.textField7.text = self.contactMobileNbr;

        if (self.actType>0 && [self.actTypeArray count]>0) {
            for (NSDictionary *dic in self.actTypeArray) {
                if ([[dic objectForKey:@"value"] intValue] == self.actType) {
                    [topCell.button5 setTitle:[dic objectForKey:@"name"] forState:UIControlStateNormal];
                }
            }
        }
        return topCell;

        
    }
    else if (indexPath.section == 2) { //高级设置
        NSString *identify =@"SecondActivityCell";
        NewSecondActivityCell *cell = (NewSecondActivityCell *)[tableView dequeueReusableCellWithIdentifier:identify];
        
        
        if (cell == nil) {
            cell = [[NewSecondActivityCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identify];
            
            CGRect frame = cell.btn3.frame;
            self.refundTableView.frame = CGRectMake(APP_VIEW_WIDTH/3, frame.origin.y+frame.size.height, APP_VIEW_WIDTH/3*2, 100);

        }
        
        cell.activityDelegate = self;
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        
        
        
        
        
        [cell.contentView addSubview:self.refundTableView];
        
        if (self.feeScaleArray.count > 0) {
            NSMutableArray *array = [[NSMutableArray alloc] init];
            for (NSDictionary *dic in self.feeScaleArray) {
                NSNumber *number = [dic objectForKey:@"price"];
                [array addObject:number];
                
            }
            cell.label01.text = [NSString stringWithFormat:@"%lu",(unsigned long)self.feeScaleArray.count];
            cell.btn3.enabled = YES;
            cell.textField4.enabled = YES;
            cell.textField5.enabled = YES;
            cell.textField6.enabled = YES;
            
            self.maxPrice = [array valueForKeyPath:@"@max.floatValue"];
            self.minPrice = [array valueForKeyPath:@"@min.floatValue"];

//            NSLog(@"max %@   min %@", _maxPrice, _minPrice);
            
            for (NSDictionary *dic in self.refundRequiredArray) {
                
                NSNumber *Value = [dic objectForKey:@"refundValue"];
                if (Value.intValue == self.refundRequiredID) {
                    self.refundRequired = [dic objectForKey:@"refundName"];
                }
                
            }
            
            
        }else {
            cell.btn3.enabled = NO;
            cell.textField4.enabled = NO;
            cell.textField5.enabled = NO;
            cell.textField6.enabled = NO;
            
            self.dayOfBooking = @"";
            self.registerNbrRequired = @"";
            self.limitedParticipators = @"";
            self.refundRequired = @"退款模式";
            self.refundRequiredID = 0;
        }
        if (self.refundRequired) {
            [cell.btn3 setTitle:self.refundRequired forState:UIControlStateNormal];
            
        }
        
        
        
        if (self.maxPrice.intValue == 0 && self.minPrice.intValue == 0) {
            cell.label02.text = @"";
        }else if (self.maxPrice.floatValue == self.minPrice.floatValue){
            cell.label02.text = [NSString stringWithFormat:@"%@元",[self.maxPrice stringValue]];
        }else {
            cell.label02.text = [NSString stringWithFormat:@"%@-%@元", self.minPrice, self.maxPrice];
        }
        
        
        cell.textField4.text = self.dayOfBooking;
        cell.textField5.text = self.registerNbrRequired; //单人报名人数限制
        cell.textField6.text = self.limitedParticipators;//活动参与人数限制
        
        return cell;
        
    }
    else if (indexPath.section == 3) {
        NSString *identifier = @"newBottomActCell";
        NewBottomActivityCell *cell = (NewBottomActivityCell *)[tableView dequeueReusableCellWithIdentifier:identifier];
        if (!cell) {
            cell = [[NewBottomActivityCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            
            
            
            UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
            button.frame = CGRectMake(15, 100+10, APP_VIEW_WIDTH-30, 35);
            button.tag = 30009;
            [button addTarget:self action:@selector(btnActivity) forControlEvents:UIControlEventTouchUpInside];
            button.backgroundColor = UICOLOR(197, 0, 11, 1);
            [button setTitle:@"保存活动" forState:UIControlStateNormal];
            [cell.contentView addSubview:button];
            
        }
        cell.backgroundColor = APP_VIEW_BACKCOLOR;
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        
        UIButton *button = [cell.contentView viewWithTag:30009];
        
        [cell.contentView addSubview:self.actTextView];
        if (self.textActSize.width > 0) {
            self.actTextView.frame = CGRectMake(self.actTextView.frame.origin.x, self.actTextView.frame.origin.y, self.textActSize.width, self.textActSize.height);
            button.frame = CGRectMake(15, self.textActSize.height+10, button.frame.size.width , button.frame.size.height);
            self.actTextView.editable = NO;
            
        }
        UIButton *btn2 = [self.actTextView viewWithTag:30010];
        btn2.frame = self.actTextView.frame;
        
        return cell;
    }
    
    
    return nil;

}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.section == 0) {

        if (IOS8){
            [self presentViewController:self.alertController animated:YES completion:nil];
        }else {
            UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:@"从相册选择" otherButtonTitles: nil];
            [actionSheet showInView:self.view];
            
        }
    }
//    if (indexPath.section == 3) {
//        [self editActView];
//        
//    }
    
    if (self.refundTableView.hidden == NO) {
        self.refundTableView.hidden = YES;
        
    }
    
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    
    if (section == 2 || section == 3) {
        
        return 40;
        
    }
    
    return 0;
    
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {

    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
    view.backgroundColor = UICOLOR(239, 239, 244, 1);
    UILabel *label = [[UILabel alloc] initWithFrame:view.bounds];
//    label.text = @"高级设置(可选)";
    label.textAlignment = NSTextAlignmentCenter;
    [view addSubview:label];
    
    if (section == 2) {
        label.text = @"高级设置(可选)";
        UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
        button.backgroundColor = [UIColor clearColor];
        button.imageEdgeInsets = UIEdgeInsetsMake(0, APP_VIEW_WIDTH-40, 0, 0);
        
        [button setImage:[UIImage imageNamed:@"garydown"] forState:UIControlStateNormal];
        [button setImage:[UIImage imageNamed:@"garyup"] forState:UIControlStateSelected];
        [button addTarget:self action:@selector(btnAdvanced:) forControlEvents:UIControlEventTouchUpInside];
        [view addSubview:button];
        
        UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(0, 39, APP_VIEW_WIDTH, 1)];
        lineView.backgroundColor = APP_CELL_LINE_COLOR;
        [view addSubview:lineView];
        
        button.selected = self.isShowAdvanced;
        return view;
        
    }else if (section == 3) {
        label.text = @"活动介绍";
        return view;
        
    }
    
    return nil;
    
}


- (void)btnAdvanced:(UIButton *)sender {
    self.isShowAdvanced = !self.isShowAdvanced;
    NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:2];
    [self.tableView  reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
    
    
}

//编辑活动
- (void)editActView {
    NewActivityIntroduceViewController *vc = [[NewActivityIntroduceViewController alloc] init];
    vc.txtContent = self.txtContent;
    [self.navigationController pushViewController:vc animated:YES];
}



#pragma mark - activity delegate
//活动类型
- (void)gotoActivityType {

    ActivityTypeViewController *acVC = [[ActivityTypeViewController alloc] init];
    acVC.actTypeArray = self.actTypeArray;
    acVC.selectCellRow = self.actType;
    [self.navigationController pushViewController:acVC animated:YES];
    
}

//开始时间
- (void)clickBeginTime {
//    NSLog(@"------>clickBeginTime");
    self.clickDatePicker = 1;
    
    
    if (self.tag) {
        UITextField *textfield = [self.tableView viewWithTag:self.tag];
        [textfield resignFirstResponder];

    }
    pickerView.hidden = NO;
    
}
//结束时间
- (void)clickOverTime {
//    NSLog(@"------>clickOverTime");
    self.clickDatePicker = 2;
    
    
    if (self.tag) {
        UITextField *textfield = [self.tableView viewWithTag:self.tag];
        [textfield resignFirstResponder];
        
    }
    pickerView.hidden = NO;
    
}



//收费规格
- (void)gotoChargeVC {
    ChargeViewController *chargeVC = [[ChargeViewController alloc] init];
    chargeVC.chargeData = [self.feeScaleArray mutableCopy];
    [self.navigationController pushViewController:chargeVC animated:YES];
    
}

//展示退款列表
- (void)showRefundList {
    if  ( self.refundTableView.hidden == NO) {
        self.refundTableView.hidden = YES;
    }else {
        self.refundTableView.hidden = NO;
    }
    
}
//
- (void)topCellTag:(UITextField *)textField {
    self.tag = textField.tag;
}


- (void)topCellTextFieldChange:(UITextField *)textField {
//    NSLog(@"------> %ld %@ ",(long)textField.tag, textField.text);
    
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:0 inSection:1];
    NewTopActivityCell *cell = [self.tableView cellForRowAtIndexPath:indexPath];
    if (textField == cell.textField1) { //活动名称
        self.activityName = cell.textField1.text;
        
    }else if (textField == cell.textField2) { //活动地点
        self.activityLocation = cell.textField2.text;
        
    }else if (textField == cell.textField6) { //联系人
        self.contactName = cell.textField6.text;
        
    }else if (textField == cell.textField7) { //联系人电话
        self.contactMobileNbr = cell.textField7.text;
        
    }
    
    
    
    indexPath = [NSIndexPath indexPathForRow:0 inSection:1];
    cell = [self.tableView cellForRowAtIndexPath:indexPath];

    
}


- (void)secondTextField:(UITextField *)textField {
    
//    NSLog(@"------> %ld %@ ",(long)textField.tag, textField.text);
    
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:0 inSection:2];
    NewSecondActivityCell *cell = [self.tableView cellForRowAtIndexPath:indexPath];

    if (textField == cell.textField4) { //提前预约天数
        self.dayOfBooking = cell.textField4.text;
        
    }else if (textField == cell.textField5) { //单人报名人数限制
        self.registerNbrRequired = cell.textField5.text;
        
    }else if (textField == cell.textField6) { //活动参与人数限制
        self.limitedParticipators = cell.textField6.text;
        
    }
    
    
    indexPath = [NSIndexPath indexPathForRow:0 inSection:2];
    cell = [self.tableView cellForRowAtIndexPath:indexPath];
    
    
    
}


//array 转json
- (NSString *)jsonStringWithArray:(NSArray *)array {
    NSString *jsonString ;
    if (array.count > 0) {
        NSError *err;
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:self.feeScaleArray
                                                           options:NSJSONWritingPrettyPrinted
                                                             error:&err];
        if (err) {
//            NSLog(@"json解析失败：%@",err);
            return nil;
        }
        
        jsonString = [[NSString alloc] initWithData:jsonData
                                           encoding:NSUTF8StringEncoding];
        
    }else {
        jsonString = @"";
    }
    
    return jsonString;
}

- (NSString *)jsonStringWithDictionary:(NSMutableDictionary *)dic {
    NSString *jsonString ;
    if (dic.count > 0) {
        NSError *err;
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic
                                                           options:NSJSONWritingPrettyPrinted
                                                             error:&err];
        if (err) {
//            NSLog(@"json解析失败：%@",err);
            return nil;
        }
        
        jsonString = [[NSString alloc] initWithData:jsonData
                                           encoding:NSUTF8StringEncoding];
        
    }else {
        jsonString = @"";
    }
    
    return jsonString;
}



#pragma mark - NSNotificationCenter
//活动类型
- (void)callReload:(NSNotification *)notification {
//    NSDictionary *actTypeDic = [notification.userInfo objectForKey:@"row"] ;
//    self.actTypeName = [actTypeDic objectForKey:@"name"];
//    self.actType = [[actTypeDic objectForKey:@"value"] intValue];
    self.actType = [[notification.userInfo objectForKey:@"row"] intValue] ;
    NSIndexSet *indexSet = [[NSIndexSet alloc] initWithIndex:1];
    [self.tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
    
}
//收费规格
- (void)callReloadPrice:(NSNotification *)notification {

    self.feeScaleArray = [notification.userInfo objectForKey:@"chargeData"];

    
    NSIndexSet *indexSet = [[NSIndexSet alloc] initWithIndex:2];
    [self.tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
    

}
//活动介绍
- (void)txtContentFinish:(NSNotification *)notification {
    self.txtContent = [notification.userInfo objectForKey:@"txtContent"];
    
    self.actTextView.attributedText = [self actAttributedWithString:self.txtContent];
    self.textActSize = self.actTextView.contentSize;
    
    NSIndexSet *indexSet = [[NSIndexSet alloc] initWithIndex:3];
    [self.tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
    
//    [self.tableView reloadData];

}



#pragma mark - Keyboard notification

- (void)onKeyboardNotification:(NSNotification *)notification {
    [self btnPickerFinish];
    //Reset constraint constant by keyboard height
    if ([notification.name isEqualToString:UIKeyboardWillShowNotification]) {
        CGRect keyboardFrame = ((NSValue *) notification.userInfo[UIKeyboardFrameEndUserInfoKey]).CGRectValue;
        
        _tableView.frame = CGRectMake(_tableView.frame.origin.x, _tableView.frame.origin.y, _tableView.frame.size.width, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-keyboardFrame.size.height);
        
    } else if ([notification.name isEqualToString:UIKeyboardWillHideNotification]) {
        _tableView.frame = CGRectMake(_tableView.frame.origin.x, _tableView.frame.origin.y, _tableView.frame.size.width, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y);
    }
    
    //Animate change
    [UIView animateWithDuration:0.8f animations:^{
        [self.view layoutIfNeeded];
    }];
    
}


- (void)getActivityInfo {
    
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    
    [params setObject:self.activityCode forKey:@"activityCode"];
 
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getActivityInfo" strParams:self.activityCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getActivityInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
//        NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        
        self.activityImg =[responseObject objectForKey:@"activityImg"] ;     // 活动图片
        
        self.activityName = [responseObject objectForKey:@"activityName"];  //活动名称
        self.startTime = [responseObject objectForKey:@"startTime"];        //开始时间
        self.endTime = [responseObject objectForKey:@"endTime"];            //活动结束时间
        
        
        self.activityLocation = [responseObject objectForKey:@"activityLocation"]; //地址
        self.txtContent =[responseObject objectForKey:@"richTextContent"];       //活动说明
        self.actType = [[responseObject objectForKey:@"actType"] intValue];               //活动类型 1-聚会，2-运动，3-户外，4-亲子，5-体验课，6-音乐
        self.contactName = [responseObject objectForKey:@"contactName"];     //联系人
        self.contactMobileNbr = [responseObject objectForKey:@"contactMobileNbr"]; //联系电话
       
        self.feeScaleArray = [responseObject objectForKey:@"feeScale"]; //收费规格

        self.refundRequiredID = [[responseObject objectForKey:@"refundRequired"] intValue]; //需要传的退款要求
        self.dayOfBooking = [responseObject objectForKey:@"dayOfBooking"];// 提前预约天数
        self.registerNbrRequired = [responseObject objectForKey:@"registerNbrRequired"]; //单人报名人数限制
        self.limitedParticipators = [responseObject objectForKey:@"limitedParticipators"]; //活动参与人数限制
        
        if (self.feeScaleArray.count > 0){//是否开启高级设置
            self.isShowAdvanced = YES;
        }else {
            self.isShowAdvanced = NO;
        }
        self.actTextView.attributedText = [self actAttributedWithString:self.txtContent];
        self.textActSize = self.actTextView.contentSize;
        [self.tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        
    }];

}

- (void)getActType {
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    //    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    ////    NSString* vcode = [gloabFunction getSign:@"getActivityInfo" strParams:self.activityCode];
    //    [params setObject:vcode forKey:@"vcode"];
    //    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getActType" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        self.actTypeArray = [NSArray arrayWithArray:responseObject];
        [self.tableView reloadData];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        
    }];
    
}



#pragma mark - 字符串 转 图文页
- (NSMutableAttributedString *)actAttributedWithString:(NSString *)str {
    NSArray *array = [self arrayWithTxtContent:str];
    
    NSMutableAttributedString * mutStr = [NSMutableAttributedString new];
    for (NSString *txtStr in array) {
        if([txtStr hasPrefix:@"/Public/Uploads/"]) {
//            [NSURL URLWithString:]
            NSURL *url = [NSURL URLWithString: [NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL, txtStr]];
            UIImage *imagea;
            imagea = [UIImage imageWithData: [NSData dataWithContentsOfURL:url]];
            imagea = [self imageCompressForWidth:imagea targetWidth:APP_VIEW_WIDTH-30];
            ActImageTextAttachment * attachment1 = [ActImageTextAttachment new] ;
            attachment1.imgUrl = [NSString stringWithFormat:@"<img src=\"%@\" /><BR />",txtStr];
            attachment1.imgSize = CGSizeMake(imagea.size.width, imagea.size.height);
            attachment1.image = imagea;
            NSAttributedString * attachStr1 = [NSAttributedString attributedStringWithAttachment:attachment1];
            [mutStr appendAttributedString:attachStr1];
        }else {
            [mutStr appendAttributedString:[[NSAttributedString alloc] initWithString:txtStr attributes:nil]];
            
            
        }

    }
    return mutStr;

    
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 0) {
        UIImagePickerController *controller = [[UIImagePickerController alloc] init];
        controller.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
        NSMutableArray *mediaTypes = [[NSMutableArray alloc] init];
        [mediaTypes addObject:(__bridge NSString *)kUTTypeImage];
        controller.mediaTypes = mediaTypes;
        controller.delegate = self;
        [self presentViewController:controller
                           animated:YES
                         completion:^(void){
                             NSLog(@"Picker View Controller is presented");
                         }];

    }
    
    
    
}



#pragma mark - textField delegate
- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
    
}

#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [picker dismissViewControllerAnimated:YES completion:^() {
        UIImage *portraitImg = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
        //            CGSize aFactSize = [[UIScreen mainScreen] bounds].size;
        portraitImg = [self imageByScalingToMaxSize:portraitImg];
        VPImageCropperViewController *imgCropperVC = [[VPImageCropperViewController alloc] initWithImage:portraitImg cropFrame:CGRectMake(0, (APP_VIEW_HEIGHT-self.view.frame.size.width*32.0/75)/2, self.view.frame.size.width, self.view.frame.size.width*32.0/75) limitScaleRatio:3.0];
        imgCropperVC.delegate = self;
        [self presentViewController:imgCropperVC animated:YES completion:^{
            // TO DO
        }];
    }];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [picker dismissViewControllerAnimated:YES completion:^(){
    }];
}

- (void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    // bug fixes: UIIMagePickerController使用中偷换StatusBar颜色的问题
    if ([navigationController isKindOfClass:[UIImagePickerController class]] &&
        ((UIImagePickerController *)navigationController).sourceType ==     UIImagePickerControllerSourceTypePhotoLibrary) {
        [[UIApplication sharedApplication] setStatusBarHidden:NO];
        [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent animated:NO];
    }
}

#pragma mark VPImageCropperDelegate
- (void)imageCropper:(VPImageCropperViewController *)cropperViewController didFinished:(UIImage *)editedImage {
    [cropperViewController dismissViewControllerAnimated:YES completion:^{
        [self httpImageRequest:editedImage];
        
    }];
}

- (void)imageCropperDidCancel:(VPImageCropperViewController *)cropperViewController {
    [cropperViewController dismissViewControllerAnimated:YES completion:^{
    }];
}

- (void)httpImageRequest:(UIImage *)image  {
    [SVProgressHUD showWithStatus:@"正在上传图片"];
    NSData *dataObj = UIImageJPEGRepresentation(image, 1);
//    NSLog(@"%lu", (long)[dataObj length] / 1000) ;

    
   
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    
    NSMutableURLRequest *request = [[AFHTTPRequestSerializer serializer] multipartFormRequestWithMethod:@"POST" URLString:[NSString stringWithFormat:@"%@/uploadImg",APP_SERVERCE_COMM_URL ] parameters:nil constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
        [formData appendPartWithFileData:dataObj name:@"imagefile" fileName:@"Icon.png" mimeType:@"image/png"];
        
        
    } error:nil];
    
    NSProgress *progressData = [NSProgress progressWithTotalUnitCount:dataObj.length];
    
    NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
    AFURLSessionManager *manager = [[AFURLSessionManager alloc] initWithSessionConfiguration:configuration];
    AFHTTPResponseSerializer *aa= [AFJSONResponseSerializer serializer];
    aa.acceptableContentTypes = [NSSet setWithObjects:@"application/json",@"text/json",@"text/javascript",@"text/html",@"text/plain",nil];
    manager.responseSerializer = aa;
    
    NSURLSessionUploadTask *uploadTask = [manager uploadTaskWithStreamedRequest:request progress:&progressData completionHandler:^(NSURLResponse *response, id responseObject, NSError *error) {
        if (error) {
            NSString *aString = [[NSString alloc] initWithData:[error.userInfo objectForKey:@"com.alamofire.serialization.response.error.data"] encoding:NSUTF8StringEncoding];
            
            [SVProgressHUD dismiss];
            
        }else {

            NSString *code = [responseObject objectForKey:@"code"];
            if (code.length>5){
                NSLog(@"imgUrl ----> %@", code);
                _activityImg = code;
                NSIndexSet *indexSet = [[NSIndexSet alloc] initWithIndex:0];
                [self.tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
                
                
            }else if ([code isEqualToString:@"80020"]) {
                CSAlert(@"图片格式不正确");
            }else if ([code isEqualToString:@"80021"]) {
                CSAlert(@"图片大小不正确");
            }
            [SVProgressHUD dismiss];
        }
    }];
    
    [uploadTask resume];
    
}


#pragma mark image scale utility
- (UIImage *)imageByScalingToMaxSize:(UIImage *)sourceImage {
    if (sourceImage.size.width < ORIGINAL_MAX_WIDTH) return sourceImage;
    CGFloat btWidth = 0.0f;
    CGFloat btHeight = 0.0f;
    if (sourceImage.size.width > sourceImage.size.height) {
        btHeight = ORIGINAL_MAX_WIDTH;
        btWidth = sourceImage.size.width * (ORIGINAL_MAX_WIDTH / sourceImage.size.height);
    } else {
        btWidth = ORIGINAL_MAX_WIDTH;
        btHeight = sourceImage.size.height * (ORIGINAL_MAX_WIDTH / sourceImage.size.width);
    }
    CGSize targetSize = CGSizeMake(btWidth, btHeight);
    return [self imageByScalingAndCroppingForSourceImage:sourceImage targetSize:targetSize];
}


- (UIImage *)imageByScalingAndCroppingForSourceImage:(UIImage *)sourceImage targetSize:(CGSize)targetSize {
    UIImage *newImage = nil;
    CGSize imageSize = sourceImage.size;
    CGFloat width = imageSize.width;
    CGFloat height = imageSize.height;
    CGFloat targetWidth = targetSize.width;
    CGFloat targetHeight = targetSize.height;
    CGFloat scaleFactor = 0.0;
    CGFloat scaledWidth = targetWidth;
    CGFloat scaledHeight = targetHeight;
    CGPoint thumbnailPoint = CGPointMake(0.0,0.0);
    if (CGSizeEqualToSize(imageSize, targetSize) == NO)
    {
        CGFloat widthFactor = targetWidth / width;
        CGFloat heightFactor = targetHeight / height;
        
        if (widthFactor > heightFactor)
            scaleFactor = widthFactor; // scale to fit height
        else
            scaleFactor = heightFactor; // scale to fit width
        scaledWidth  = width * scaleFactor;
        scaledHeight = height * scaleFactor;
        
        // center the image
        if (widthFactor > heightFactor)
        {
            thumbnailPoint.y = (targetHeight - scaledHeight) * 0.5;
        }
        else
            if (widthFactor < heightFactor)
            {
                thumbnailPoint.x = (targetWidth - scaledWidth) * 0.5;
            }
    }
    UIGraphicsBeginImageContext(targetSize); // this will crop
    CGRect thumbnailRect = CGRectZero;
    thumbnailRect.origin = thumbnailPoint;
    thumbnailRect.size.width  = scaledWidth;
    thumbnailRect.size.height = scaledHeight;
    
    [sourceImage drawInRect:thumbnailRect];
    
    newImage = UIGraphicsGetImageFromCurrentImageContext();
    if(newImage == nil) NSLog(@"could not scale image");
    
    //pop the context to get back to the default
    UIGraphicsEndImageContext();
    return newImage;
}


-(UIImage *) imageCompressForWidth:(UIImage *)sourceImage targetWidth:(CGFloat)defineWidth{
    
    UIImage *newImage = nil;
    CGSize imageSize = sourceImage.size;
    CGFloat width = imageSize.width;
    CGFloat height = imageSize.height;
    CGFloat targetWidth = defineWidth;
    CGFloat targetHeight = height / (width / targetWidth);
    CGSize size = CGSizeMake(targetWidth, targetHeight);
    CGFloat scaleFactor = 0.0;
    CGFloat scaledWidth = targetWidth;
    CGFloat scaledHeight = targetHeight;
    CGPoint thumbnailPoint = CGPointMake(0.0, 0.0);
    
    if(CGSizeEqualToSize(imageSize, size) == NO){
        
        CGFloat widthFactor = targetWidth / width;
        CGFloat heightFactor = targetHeight / height;
        
        if(widthFactor > heightFactor){
            scaleFactor = widthFactor;
        }
        else{
            scaleFactor = heightFactor;
        }
        scaledWidth = width * scaleFactor;
        scaledHeight = height * scaleFactor;
        
        if(widthFactor > heightFactor){
            
            thumbnailPoint.y = (targetHeight - scaledHeight) * 0.5;
            
        }else if(widthFactor < heightFactor){
            
            thumbnailPoint.x = (targetWidth - scaledWidth) * 0.5;
        }
    }
    
    UIGraphicsBeginImageContext(size);
    
    CGRect thumbnailRect = CGRectZero;
    thumbnailRect.origin = thumbnailPoint;
    thumbnailRect.size.width = scaledWidth;
    thumbnailRect.size.height = scaledHeight;
    
    [sourceImage drawInRect:thumbnailRect];
    
    newImage = UIGraphicsGetImageFromCurrentImageContext();
    
    if(newImage == nil){
        
        NSLog(@"scale image fail");
    }
    UIGraphicsEndImageContext();
    return newImage;
}

@end
