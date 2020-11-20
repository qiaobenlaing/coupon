//
//  BMSQ_ActivitySettingViewController.m
//  BMSQS
//
//  Created by Sencho Kong on 15/8/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_RedPackageSettingViewController.h"
#import "DateInputTableViewCell.h"
#import "StringInputTableViewCell.h"


@interface BMSQ_RedPackageSettingViewController ()<UITableViewDataSource,UITableViewDelegate,DateInputTableViewCellDelegate>{
    
    BOOL enableLimitNumber;  // 是否限制红包数量
}

@property (nonatomic ,strong) UITableView* tableView;
@property (nonatomic ,strong) UIImageView *checkBoxImageView;
@property (nonatomic ,strong) UITextField *lowerPriceTextFiled;  // 上限
@property (nonatomic ,strong) UITextField *upperPriceTextFiled;  // 下限
@property (nonatomic ,weak) UITextField *totalVolumeTextFiled;   // 总量
@property (nonatomic ,weak) UITextField *totalValueTextFiled;    // 总额
@property (nonatomic ,weak) UITextField *validityPeriodTextField;// 有效天数
@property (nonatomic ,weak) UITextField *nbrPerDayTextField;     // 每天限发数量，0为无限制
@property (nonatomic ,strong) NSDate *startDate;                 // 开始时间
@property (nonatomic ,strong) NSDate *endDate;                   // 结束时间

@end

@implementation BMSQ_RedPackageSettingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:@"红包设置"];
    [self setNavBackItem];
    
    UIButton* btnSetting = [UIButton buttonWithType:UIButtonTypeCustom];
    btnSetting.frame = CGRectMake(APP_VIEW_WIDTH - 50, APP_STATUSBAR_HEIGHT, 44, 44);
    [btnSetting setTitle:@"提交" forState:UIControlStateNormal];
    [btnSetting addTarget:self action:@selector(btnSettingClick) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:btnSetting];
    
    _tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, self.view.frame.size.width, APP_VIEW_CAN_USE_HEIGHT+44)];
    _tableView.dataSource = self;
    _tableView.delegate = self;
    _tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    
    UIView *footerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, _tableView.frame.size.width, 10)];
    _tableView.tableFooterView = footerView;
    [self.view addSubview:_tableView];
    
    _checkBoxImageView = [[UIImageView alloc] initWithFrame:CGRectZero];
    _checkBoxImageView.image = [UIImage imageNamed:@"选中"];
    enableLimitNumber = YES;

    _upperPriceTextFiled = [[UITextField alloc] initWithFrame:CGRectZero];
    _upperPriceTextFiled.font = [UIFont systemFontOfSize:14.0];
    _upperPriceTextFiled.borderStyle = UITextBorderStyleNone;
    _upperPriceTextFiled.textAlignment = NSTextAlignmentCenter;
    
    _lowerPriceTextFiled = [[UITextField alloc] initWithFrame:CGRectZero];
    _lowerPriceTextFiled.font = [UIFont systemFontOfSize:14.0];
    _lowerPriceTextFiled.borderStyle = UITextBorderStyleNone;
    _lowerPriceTextFiled.textAlignment = NSTextAlignmentCenter;
    
}

- (void)btnSettingClick{
    
  
    //1 红包归属     bonusBelonging
    //2 归属商店编码  shopCode
    //3 创建者编码   creatorCode
    //4 金额区间（上限） upperPrice
    //5 金额区间（下限）lowerPrice
    //6 发行红包总额 totalValue
    //7 每天限制发红包数量 nbrPerDay
    //8 发行总数量 totalVolume
    //9 红包使用有效期 validityPeriod
    //10 红包活动开始时间 startTime
    //11 红包活动结束时间 endTime
    
    if (!_upperPriceTextFiled.text || _upperPriceTextFiled.text.length == 0) {
        return;
    }
    if (!_lowerPriceTextFiled.text || _lowerPriceTextFiled.text.length == 0) {
        return;
    }
    if (!_totalValueTextFiled.text || _totalValueTextFiled.text.length == 0) {
        return;
    }
    if (!_nbrPerDayTextField.text || _nbrPerDayTextField.text.length == 0) {
        return;
    }
    if (!_totalVolumeTextFiled.text || _totalVolumeTextFiled.text.length == 0) {
        return;
    }
    if (!_validityPeriodTextField.text || _validityPeriodTextField.text.length == 0) {
        return;
    }
    
    if (!_startDate || !_endDate) {
        return;
    }
    
    NSDateFormatter *dataFormatter = [[NSDateFormatter alloc] init];
    dataFormatter.dateFormat = @"YYYY-MM-DD hh:mm:ss";
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    NSString* vcode = [gloabFunction getSign:@"addBonus" strParams:@"1"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [params setObject:@"1" forKey:@"bonusBelonging"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getStaffCode] forKey:@"creatorCode"];
    [params setObject:_upperPriceTextFiled.text forKey:@"upperPrice"];
    [params setObject:_lowerPriceTextFiled.text forKey:@"lowerPrice"];
    [params setObject:_totalValueTextFiled.text forKey:@"totalValue"];
    [params setObject:_nbrPerDayTextField.text forKey:@"nbrPerDay"];
    [params setObject:_totalVolumeTextFiled.text forKey:@"totalVolume"];
    [params setObject:_validityPeriodTextField.text forKey:@"validityPeriod"];
    [params setObject:[dataFormatter stringFromDate:_startDate] forKey:@"startTime"];
    [params setObject:[dataFormatter stringFromDate:_endDate] forKey:@"endTime"];
    
    
    __block typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"addBonus" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        
        /*
         50000-成功；
         20000-失败，请重试；
         50700-红包名字不正确；
         50701-红包所属类型不正确；
         50702-创建者编码不正确；
         50314-商家编码不正确；
         50704-红包金额上限不正确；
         50705-红包金额下限不正确；
         50706-发行总额不正确；
         50707-每天限发数量不正确；
         50708-发行总量不正确；
         50709-开抢时间不正确；
         50710-结束时间不正确；
         50711-开始使用时间不正确；
         50712-截止使用时间不正确；
         50713-红包有效期不正确；
         50716-红包最大金额不得小于最小；
         50718-抢红包开始时间不能大于结束时间；
         50719-红包发行总额过低；
         50727-红包发行总额过高
         */
        switch (resCode) {
            case 5000:{
                [weakSelf.navigationController popViewControllerAnimated:YES];
            }
                
                break;
            case 20000:
                 CSAlert(@"失败，请重试");
                break;
            case 50700:
                CSAlert(@"红包名字不正确");
                break;
            case 50701:
                CSAlert(@"红包所属类型不正确");
                break;
            case 50702:
                CSAlert(@"创建者编码不正确");
                break;
            case 50314:
                CSAlert(@"商家编码不正确");
                break;
            case 50704:
                CSAlert(@"红包金额上限不正确");
                break;
            case 50705:
                CSAlert(@"红包金额下限不正确");
                break;
            case 50706:
                CSAlert(@"发行总额不正确");
                break;
            case 50707:
                CSAlert(@"每天限发数量不正确");
                break;
            case 50708:
                CSAlert(@"发行总量不正确");
                break;
            case 50709:
                CSAlert(@"开抢时间不正确");
                break;
            case 50710:
                CSAlert(@"结束时间不正确");
                break;
            case 50711:
                CSAlert(@"开始使用时间不正确");
                break;
            case 50712:
                CSAlert(@"截止使用时间不正确");
                break;
            case 50713:
                CSAlert(@"红包有效期不正确");
                break;
            case 50716:
                CSAlert(@"红包最大金额不得小于最小");
                break;
            case 50718:
                CSAlert(@"抢红包开始时间不能大于结束时间");
                break;
            case 50719:
                CSAlert(@"红包发行总额过低");
                break;
            case 50727:
                CSAlert(@"红包发行总额过高");
                break;
                
            default:
                break;
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
    }];
    
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    return 8;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.row == 3 ||indexPath.row == 4) {
        
        static NSString *cellID0 = @"UITableViewCell0";
        DateInputTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID0];
        
        if (!cell) {
            cell = [[DateInputTableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:cellID0];
        }
        cell.delegate = self;
        cell.tag = indexPath.row;
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        if (indexPath.row == 3) {
            cell.textLabel.text = @"开始时间";
        }else{
            cell.textLabel.text = @"结束时间";
        }
        
        return cell;
        
    }else  if (indexPath.row == 6 || indexPath.row == 1){
        
        static NSString *cellID1 = @"reuseIdentifier1";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID1];
        if (!cell) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellID1];
        }
        
        if (indexPath.row == 1){
            
            cell.textLabel.text = @"金额区间             到";
            CGSize textSize = [cell.textLabel sizeThatFits:CGSizeZero];
            _lowerPriceTextFiled.frame = CGRectMake(85, 10, 60, 24);
            _upperPriceTextFiled.frame = CGRectMake(textSize.width + 20,10, 60, 24);
            [cell.contentView addSubview:_upperPriceTextFiled];
            [cell.contentView addSubview:_lowerPriceTextFiled];
            
        }else{
            cell.contentView.backgroundColor = self.view.backgroundColor;
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.textLabel.text = @"        每天限制发送红包数量";
            
            _checkBoxImageView.frame = CGRectMake(10, 0, 44, 44);
            if (enableLimitNumber) {
                _checkBoxImageView.image = [UIImage imageNamed:@"选中"];
            }else{
                _checkBoxImageView.image = [UIImage imageNamed:@"未选中"];
            }
            [cell.contentView addSubview:_checkBoxImageView];
            
        }
        
        return cell;

    }else {
        
        static NSString *cellID = @"reuseIdentifier";
        StringInputTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID];
        if (!cell) {
            cell = [[StringInputTableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:cellID];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.tag = indexPath.row;
        
        if (indexPath.row == 0) {
            
            cell.textLabel.text = @"红包数量";
            _totalVolumeTextFiled = cell.textField;
            
        }else if (indexPath.row == 2){
            
            cell.textLabel.text = @"红包总额";
            _totalValueTextFiled = cell.textField;
        }
        else if (indexPath.row == 5){
            
            cell.textLabel.text = @"领取红包使用有效期";
            cell.detailTextLabel.text = @"天";
            _validityPeriodTextField = cell.textField;
        }
        else if (indexPath.row == 7){
            
            cell.textField.placeholder = @"请输入红包数量";
            _nbrPerDayTextField = cell.textField;
        }

        
        return cell;
        
    }
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == 6) {
        if (enableLimitNumber) {
            enableLimitNumber = NO;
            _nbrPerDayTextField.text = @"0";
        }else{
            enableLimitNumber = YES;
            _nbrPerDayTextField.text = nil;
        }
        [tableView reloadData];
    }
    
}



- (void)tableViewCell:(DateInputTableViewCell *)cell didEndEditingWithDate:(NSDate *)value{
    
    if (cell.tag == 3) {
        _startDate = value;
    }else{
        _endDate   = value;
    }
}


- (void)tableViewCell:(StringInputTableViewCell *)cell didEndEditingWithString:(NSString *)value{
    
    
}


- (IBAction)didClickNextButton:(id)sender {


}

@end
