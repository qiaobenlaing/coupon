//
//  BMSQ_CouponDZViewController.m
//  BMSQS
//
//  Created by Sencho Kong on 15/8/24.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_CouponDZViewController.h"
#import "DateInputTableViewCell.h"

@interface BMSQ_CouponDZViewController ()<UITableViewDataSource,UITableViewDelegate,DateInputTableViewCellDelegate>

@property (nonatomic ,strong) NSArray *data;
@property (nonatomic ,strong) UITableView* tableView;
@property (nonatomic ,strong) UIButton *applyButton;
@property (nonatomic ,strong) NSDate *startDate;
@property (nonatomic ,strong) NSDate *endDate;

@end

@implementation BMSQ_CouponDZViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavTitle:@"优惠券对账"];
    [self setNavBackItem];
    
    self.applyButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [_applyButton setBackgroundColor:UICOLOR(177, 0, 0, 1.0)];
    [_applyButton setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [_applyButton setTitle:@"统计" forState:UIControlStateNormal];
    [_applyButton addTarget:self action:@selector(didClickApplyButton:) forControlEvents:UIControlEventTouchUpInside];
    _applyButton.layer.cornerRadius = 5.0;
    
    
   
}

- (void)viewWillLayoutSubviews{
    [super viewWillLayoutSubviews];

    _tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, self.view.frame.size.width, APP_VIEW_CAN_USE_HEIGHT+44)];
    _tableView.dataSource = self;
    _tableView.delegate = self;
    _tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    
    UIView *footerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, _tableView.frame.size.width, 10)];
    _tableView.tableFooterView = footerView;
    [self.view addSubview:_tableView];

    _applyButton.frame = CGRectMake(15, 10, _tableView.frame.size.width - 30, 44);
}


- (void)didClickApplyButton:(id)sender{
    
   [self getCouponBillWithStartDate:_startDate endDate:_endDate];
    
}

#pragma mark - Request
- (void)getCouponBillWithStartDate:(NSDate *)startDate endDate:(NSDate *)endDate
{
   
    if (!startDate || !endDate) {
        return;
    }
    NSDateFormatter *dataFormatter = [[NSDateFormatter alloc] init];
    dataFormatter.dateFormat = @"YYYY-MM-DD hh:mm:ss";
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    NSString* vcode = [gloabFunction getSign:@"getCouponBill" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[dataFormatter stringFromDate:startDate] forKey:@"startDate"];
    [params setObject:[dataFormatter stringFromDate:endDate] forKey:@"endDate"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __block typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getCouponBill" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        weakSelf.data = responseObject;
        [weakSelf.tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
    }];
}


#pragma mark - DateInputTableViewCell delegate

- (void)tableViewCell:(DateInputTableViewCell *)cell didEndEditingWithDate:(NSDate *)value{
    
    if (cell.tag == 0) {
        _startDate = value;
    }
    if (cell.tag == 1) {
        _endDate = value;
    }
}


#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    if (self.data) {
        return self.data.count + 1;
    }else{
        return 1;
    }
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    return 3;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    if (indexPath.section == 0) {
        if (indexPath.row == 2) {
            
            static NSString *cellID = @"UITableViewCell1";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID];
            
            if (!cell) {
                cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:cellID];
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            [cell.contentView addSubview:_applyButton];
            return cell;
            
        }else{
            
            static NSString *cellID = @"UITableViewCell0";
            DateInputTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID];
            
            if (!cell) {
                cell = [[DateInputTableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:cellID];
            }
            cell.delegate = self;
            cell.tag = indexPath.row;
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            if (indexPath.row == 0) {
                cell.textLabel.text = @"开始时间";
            }else{
                cell.textLabel.text = @"结束时间";
            }
            return cell;
        }

    }else{
        static NSString *cellID = @"UITableViewCell2";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID];
        
        if (!cell) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:cellID];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        NSDictionary *dic = _data[indexPath.section -1] ;
        
        if (indexPath.row == 0) {
            NSInteger couponType = [dic[@"couponType"] integerValue];
            NSString *title = @"";
            
            //1-N元购；2-满就送；3-满就减；4-限时购；5-实物券；6-体验券
            if (couponType == 1) {
                title = [NSString stringWithFormat:@"%@消费",@"N元购"];
            }else if (couponType == 2){
                title = [NSString stringWithFormat:@"%@消费",@"满就送"];
            }else if (couponType == 3){
                title = [NSString stringWithFormat:@"%@消费",@"满就减"];
            }else if (couponType == 4){
                title = [NSString stringWithFormat:@"%@消费",@"限时购"];
            }else if (couponType == 5){
                title = [NSString stringWithFormat:@"%@消费",@"实物券"];
            }else if (couponType == 6){
                title = [NSString stringWithFormat:@"%@消费",@"体验券"];
            }
            cell.textLabel.text = title;
            cell.detailTextLabel.text = dic[@"usedCount"];
        }
        
        if (indexPath.row == 1) {
            cell.textLabel.text = @"共计";
            cell.detailTextLabel.text = [NSString stringWithFormat:@"%i", [dic[@"usedAmount"] integerValue]];
        }
        
        if (indexPath.row == 2) {
            cell.textLabel.text = @"其中惠圈出";
            cell.detailTextLabel.text = [NSString stringWithFormat:@"%i", [dic[@"hqAmount"] integerValue]];
        }
        
        return cell;
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0 && indexPath.row == 2) {
        return 64;
    }
    return 44;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 20;
}

@end
