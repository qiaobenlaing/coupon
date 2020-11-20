//
//  OpenNewClassTeacherViewController.m
//  BMSQS
//
//  Created by gh on 16/3/16.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "OpenNewClassTeacherViewController.h"
#import "OpenClassUtil.h"
#import "SVProgressHUD.h"
#import "MJRefresh.h"

@interface TeacherCell : UITableViewCell

@property (nonatomic, strong)NSDictionary *cellData;

@property (nonatomic, strong)UILabel *teacherNameLB;
@property (nonatomic, strong)UILabel *teacherTitleLB;


@end

@implementation TeacherCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.teacherNameLB = [OpenClassUtil openClassSetLabel:CGRectMake(10, 0, 100, 44) text:@"" font:[UIFont systemFontOfSize:14.f] textColor:nil view:self.contentView];
        self.teacherTitleLB = [OpenClassUtil openClassSetLabel:CGRectMake(100, 0, APP_VIEW_WIDTH-110, 44) text:@"" font:[UIFont systemFontOfSize:14.f] textColor:nil view:self.contentView];
        
        [gloabFunction ggsetLineView:CGRectMake(0, 43, APP_VIEW_WIDTH, 1) view:self.contentView];
        
    }
    
    return self;
}

- (void)setCellValue:(NSDictionary *)dic {
    self.cellData = dic;
    self.teacherNameLB.text = [dic objectForKey:@"teacherName"];
    self.teacherTitleLB.text = [dic objectForKey:@"teacherTitle"];
    
}


@end


@interface OpenNewClassTeacherViewController () <UITableViewDataSource, UITableViewDelegate>
{
    int page;
}
@property (nonatomic, strong)NSMutableArray *teacherArray;

@property (nonatomic, strong)UITableView *tableView;


@end

@implementation OpenNewClassTeacherViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.

    [self setViewUp];
    

}


- (void)setViewUp {
    
    [self setNavBackItem];
    [self setNavTitle:@"选择老师"];

    self.teacherArray = [[NSMutableArray alloc] init];
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
    
    [self.view addSubview:self.tableView];
    
    [self getShopTeacherList];
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.teacherArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 44;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *identifier = @"teacherCell";
    TeacherCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (!cell) {
        cell = [[TeacherCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    [cell setCellValue:[self.teacherArray objectAtIndex:indexPath.row]];
    
    return cell;
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    
    NSDictionary *dic = [[NSDictionary alloc] initWithObjectsAndKeys:self.teacherArray[indexPath.row],@"teacherData",  nil];
    NSNotification *notification =[NSNotification notificationWithName:@"selectTeacher"
                                                                object:nil
                                                              userInfo:dic];
    
    [[NSNotificationCenter defaultCenter]postNotification:notification];
    [self.navigationController popViewControllerAnimated:YES];
    
    
    
}


- (void)getShopTeacherList {
    
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSNumber numberWithInt:page] forKey:@"page"];
    
    NSString* vcode = [gloabFunction getSign:@"getShopTeacherList" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getShopTeacherList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        [self.tableView footerEndRefreshing];
        
        if (page == 1) {
            [weakSelf.teacherArray removeAllObjects];
        }
        
        
        

        [weakSelf.teacherArray addObjectsFromArray:responseObject];
        [weakSelf.tableView reloadData];
        

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        [self.tableView footerEndRefreshing];
    }];

    
}



@end
